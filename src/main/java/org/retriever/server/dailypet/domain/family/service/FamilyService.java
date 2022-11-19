package org.retriever.server.dailypet.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.request.*;
import org.retriever.server.dailypet.domain.family.dto.response.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyNameException;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyRoleNameException;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyQueryRepository;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.global.utils.invitationcode.InvitationCodeUtil;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyService {

    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;
    private final FamilyQueryRepository familyQueryRepository;
    private final SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public void validateFamilyName(ValidateFamilyNameRequest dto) {
        if (familyRepository.findByFamilyName(dto.getFamilyName()).isPresent()) {
            throw new DuplicateFamilyNameException();
        }
    }

    @Transactional(readOnly = true)
    public void validateFamilyRoleName(Long familyId, ValidateFamilyRoleNameRequest dto) {
        List<FamilyMember> familyMemberList = familyQueryRepository.findMembersByFamilyId(familyId);

        if (familyMemberList.stream()
                .anyMatch(x -> x.getMember()
                        .getFamilyRoleName()
                        .equals(dto.getFamilyRoleName())
                )
        ) {
            throw new DuplicateFamilyRoleNameException();
        }
    }

    public CreateFamilyResponse createFamily(CreateFamilyRequest dto) throws IOException {

        // 멤버 조회 및 권한 지정
        Member member = securityUtil.getMemberByUserDetails();
        member.createGroup(dto.getFamilyRoleName());

        // 새로운 가족 그룹 생성 - 초대코드 생성
        String invitationCode = InvitationCodeUtil.createInvitationCode();
        Family newFamily = Family.createFamily(dto, invitationCode);
        FamilyMember familyMember = FamilyMember.createFamilyMember(member, newFamily);

        // 연관관계 편의 메서드 - family.familyMemberList에 add & cascade.All 옵션을 통해 familyMember 자동 persist
        newFamily.insertNewMember(familyMember);
        familyRepository.save(newFamily);

        return CreateFamilyResponse.builder()
                .familyId(newFamily.getFamilyId())
                .build();
    }
    // TODO : 가족 그룹 생성 중복 로직 디자인패턴 적용해보기
    public CreateFamilyResponse createFamilyAlone() {
        Member member = securityUtil.getMemberByUserDetails();
        member.createGroup(null);

        Family familyAlone = Family.createFamilyAlone();
        FamilyMember familyMember = FamilyMember.createFamilyMember(member, familyAlone);

        familyAlone.insertNewMember(familyMember);
        familyRepository.save(familyAlone);

        return CreateFamilyResponse.builder()
                .familyId(familyAlone.getFamilyId())
                .build();
    }

    @Transactional(readOnly = true)
    public FindFamilyWithInvitationCodeResponse findFamilyWithInvitationCode(String code) {
        Family family = familyRepository.findByInvitationCode(code).orElseThrow(FamilyNotFoundException::new);
        List<FamilyMember> familyMembers = familyQueryRepository.findMembersByFamilyId(family.getFamilyId());

        return FindFamilyWithInvitationCodeResponse.of(family, familyMembers);
    }

    public EnterFamilyResponse enterFamily(Long familyId, EnterFamilyRequest dto) {
        Member member = securityUtil.getMemberByUserDetails();

        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);

        member.changeFamilyRoleName(dto.getFamilyRoleName());
        member.changeProgressStatusToPet();

        FamilyMember familyMember = FamilyMember.createFamilyMember(member, family);

        family.insertNewMember(familyMember);

        return EnterFamilyResponse.of(member, family);
    }

    public ChangeGroupTypeResponse changeGroupType(Long familyId, ChangeGroupTypeRequest dto) {

        // 멤버 조회 및 권한 지정
        Member member = securityUtil.getMemberByUserDetails();
        member.changeFamilyRoleName(dto.getFamilyRoleName());

        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);

        family.changeGroupType(dto, InvitationCodeUtil.createInvitationCode());

        return ChangeGroupTypeResponse.from(family);
    }

    public GetGroupResponse getGroupInfo(Long familyId) {
        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);
        List<FamilyMember> familyMembers = familyQueryRepository.findMembersByFamilyId(familyId);

        return GetGroupResponse.of(family, familyMembers);
    }

    public void delegateGroupLeader(Long memberId, Long familyId) {
        Member requestMember = securityUtil.getMemberByUserDetails();
        Member targetMember = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        targetMember.setFamilyLeader();
        requestMember.setGroupMember();
    }
}
