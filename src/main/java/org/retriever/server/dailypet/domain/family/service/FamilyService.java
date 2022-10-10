package org.retriever.server.dailypet.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.EnterFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyRoleNameRequest;
import org.retriever.server.dailypet.domain.family.dto.response.CreateFamilyResponse;
import org.retriever.server.dailypet.domain.family.dto.response.FindFamilyWithInvitationCodeResponse;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyNameException;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyRoleNameException;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyQueryRepository;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.global.utils.invitationcode.InvitationCodeUtil;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final FamilyQueryRepository familyQueryRepository;
    private final SecurityUtil securityUtil;

    public void validateFamilyName(ValidateFamilyNameRequest dto) {
        if (familyRepository.findByFamilyName(dto.getFamilyName()).isPresent()) {
            throw new DuplicateFamilyNameException();
        }
    }

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

    @Transactional
    public CreateFamilyResponse createFamily(CreateFamilyRequest dto) throws IOException {

        // 멤버 조회 및 권한 지정
        Member member = securityUtil.getMemberByUserDetails();
        member.setFamilyLeader();
        member.changeFamilyRoleName(dto.getFamilyRoleName());
        member.changeProgressStatusToGroup();

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

    public FindFamilyWithInvitationCodeResponse findFamilyWithInvitationCode(String code) {
        Family family = familyRepository.findByInvitationCode(code).orElseThrow(FamilyNotFoundException::new);
        List<FamilyMember> familyMembers = familyQueryRepository.findMembersByFamilyId(family.getFamilyId());

        return FindFamilyWithInvitationCodeResponse.of(family, familyMembers);
    }

    @Transactional
    public void enterFamily(Long familyId, EnterFamilyRequest dto) {
        Member member = securityUtil.getMemberByUserDetails();

        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);

        member.changeFamilyRoleName(dto.getFamilyRoleName());

        FamilyMember familyMember = FamilyMember.createFamilyMember(member, family);

        family.insertNewMember(familyMember);
    }
}
