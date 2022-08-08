package org.retriever.server.dailypet.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyRoleNameRequest;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyNameException;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyRoleNameException;
import org.retriever.server.dailypet.domain.family.repository.FamilyMemberRepository;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
}
