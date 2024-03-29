package org.retriever.server.dailypet.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.family.repository.FamilyQueryRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.retriever.server.dailypet.domain.petcare.repository.CareLogQueryRepository;
import org.retriever.server.dailypet.domain.petcare.repository.PetCareQueryRepository;
import org.retriever.server.dailypet.domain.report.dto.response.*;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SecurityUtil securityUtil;
    private final CareLogQueryRepository careLogQueryRepository;
    private final FamilyQueryRepository familyQueryRepository;
    private final PetCareQueryRepository petCareQueryRepository;

    public GetMyContributionResponse getMyContribution(LocalDate startDate, LocalDate endDate, Long petId) {
        Member member = securityUtil.getMemberByUserDetails();
        List<CareLog> careLogListBetweenDate = careLogQueryRepository.findCareLogPerPetBetweenDate(startDate, endDate, petId);

        return GetMyContributionResponse.from(getPercentByMemberId(careLogListBetweenDate, member.getId()));
    }

    public GetContributionsDetailResponse getContributionsDetail(Long familyId, Long petId, LocalDate startDate, LocalDate endDate) {
        ArrayList<MemberContributionDetail> contributionDetails = new ArrayList<>();
        List<FamilyMember> members = familyQueryRepository.findMembersByFamilyId(familyId);
        List<CareLog> careLogList = careLogQueryRepository.findCareLogPerPetFetchJoinPetCareAndMemberBetweenDate(startDate, endDate, petId);
        List<PetCare> petCareList = petCareQueryRepository.findByPetIdFetchJoinCareAlarm(petId);

        for (FamilyMember familyMember : members) {
            Member member = familyMember.getMember();
            ArrayList<CareInfoDetail> careInfoDetails = new ArrayList<>();
            for (PetCare petCare : petCareList) {
                careInfoDetails.add(CareInfoDetail.of(petCare, getCountByMemberIdAndCareId(careLogList, member, petCare)));
            }
            contributionDetails.add(MemberContributionDetail.of(member.getFamilyRoleName(), getPercentByMemberId(careLogList, member.getId()), careInfoDetails));
        }

        contributionDetails.sort((o1, o2) -> Float.compare(o2.getContributionPercent(), o1.getContributionPercent()));

        int rank = 1;
        for (MemberContributionDetail contributionDetail : contributionDetails) {
            contributionDetail.setRanking(rank++);
        }

        return GetContributionsDetailResponse.from(contributionDetails);
    }

    public GetContributionGraphResponse getMemberCountPerPetCare(Long familyId, Long petId, LocalDate startDate, LocalDate endDate) {
        List<GraphView> graphViewList = new ArrayList<>();
        List<FamilyMember> members = familyQueryRepository.findMembersByFamilyId(familyId);
        List<CareLog> careLogList = careLogQueryRepository.findCareLogPerPetFetchJoinPetCareAndMemberBetweenDate(startDate, endDate, petId);
        List<PetCare> petCareList = petCareQueryRepository.findByPetIdFetchJoinCareAlarm(petId);

        for (PetCare petCare : petCareList) {
            graphViewList.add(GraphView.createTitleView(petCare.getCareName()));
            List<CareCountInfo> careCountInfoList = new ArrayList<>();
            for (FamilyMember familyMember : members) {
                Member member = familyMember.getMember();
                careCountInfoList.add(CareCountInfo.of(member.getFamilyRoleName(), getCountByMemberIdAndCareId(careLogList, member, petCare)));
            }
            graphViewList.add(GraphView.createContentView(petCare.getCareName(), careCountInfoList));
        }
        return GetContributionGraphResponse.from(graphViewList);
    }

    private float getPercentByMemberId(List<CareLog> careLogList, Long memberId) {
        int totalCount = careLogList.size();
        if (totalCount == 0) {
            return 0;
        }
        int myCount = getCountByMemberId(careLogList, memberId);
        return Math.round( (float)myCount / totalCount * 100);
    }

    private int getCountByMemberId(List<CareLog> careLogList, Long memberId) {
        return (int) careLogList.stream()
                .filter(careLog -> memberId.equals(careLog.getMember().getId()))
                .count();
    }

    private int getCountByMemberIdAndCareId(List<CareLog> careLogList, Member member, PetCare petCare) {
        return (int) careLogList.stream()
                .filter(careLog -> member.getId().equals(careLog.getMember().getId()))
                .filter(careLog -> petCare.getPetCareId().equals(careLog.getPetCare().getPetCareId()))
                .count();
    }
}
