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
import org.retriever.server.dailypet.domain.family.repository.FamilyMemberRepository;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.retriever.server.dailypet.global.utils.invitationcode.InvitationCodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public void validateFamilyName(ValidateFamilyNameRequest dto) {
        if (familyRepository.findByFamilyName(dto.getFamilyName()).isPresent()) {
            throw new DuplicateFamilyNameException();
        }
    }

    // TODO : 쿼리 직접 만들어서 한 번에 해당 가족에 속한 member 참조하기 (현재는 familyMember.getMember()로 가져옴)
    public void validateFamilyRoleName(CustomUserDetails userDetails, ValidateFamilyRoleNameRequest dto) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(MemberNotFoundException::new);
        List<FamilyMember> familyMemberList = member.getFamilyMemberList();

        if (familyMemberList.stream()
                .anyMatch(x -> x.getMember().getFamilyRoleName().equals(dto.getFamilyRoleName()))) {
            throw new DuplicateFamilyRoleNameException();
        }
    }

    @Transactional
    public CreateFamilyResponse createFamily(CustomUserDetails userDetails, CreateFamilyRequest dto) {

        // 멤버 조회 및 권한 지정
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(MemberNotFoundException::new);
        member.setFamilyLeader();

        // 새로운 가족 그룹 생성 - 초대코드 생성
        String invitationCode = InvitationCodeUtil.createInvitationCode();
        Family newFamily = Family.createFamily(dto, invitationCode);
        FamilyMember familyMember = FamilyMember.createFamilyMember(member, newFamily);

        // 연관관계 편의 메서드 - family.familyMemberList에 add & cascade.All 옵션을 통해 familyMember 자동 persist
        newFamily.insertNewMember(familyMember);
        familyRepository.save(newFamily);

        return CreateFamilyResponse.builder()
                .familyName(newFamily.getFamilyName())
                .invitationCode(invitationCode)
                .build();
    }

    public FindFamilyWithInvitationCodeResponse findFamilyWithInvitationCode(String code) {
        Family family = familyRepository.findByInvitationCode(code).orElseThrow(FamilyNotFoundException::new);

        return FindFamilyWithInvitationCodeResponse.from(family);
    }

    @Transactional
    public void enterFamily(CustomUserDetails userDetails, Long familyId, EnterFamilyRequest dto) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(MemberNotFoundException::new);

        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);

        member.changeFamilyRoleName(dto.getFamilyRoleName());

        FamilyMember familyMember = FamilyMember.createFamilyMember(member, family);

        family.insertNewMember(familyMember);
    }
}
