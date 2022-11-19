package org.retriever.server.dailypet.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyQueryRepository;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.*;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.*;
import org.retriever.server.dailypet.domain.member.repository.MemberQueryRepository;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.exception.PetNotFoundException;
import org.retriever.server.dailypet.domain.pet.repository.PetQueryRepository;
import org.retriever.server.dailypet.domain.pet.repository.PetRepository;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;
import org.retriever.server.dailypet.global.utils.LocalDateTimeUtils;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;
    private final PetRepository petRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PetQueryRepository petQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final FamilyQueryRepository familyQueryRepository;
    private final SecurityUtil securityUtil;

    public SnsLoginResponse checkMemberAndLogin(SnsLoginRequest dto) {

        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        Family family = Family.builder().familyId(0L).build();
        List<FamilyMember> familyList = memberQueryRepository.findFamilyByMemberId(member.getId());

        if (!familyList.isEmpty()) {
            family = familyList.get(0).getFamily();
        }

        if (!member.getProviderType().equals(dto.getProviderType())) {
            throw new DifferentProviderTypeException();
        }
        String token = jwtTokenProvider.createToken(dto.getEmail());

        return SnsLoginResponse.of(member, family, token);
    }

    public void validateMemberNickName(ValidateMemberNicknameRequest dto) {
        if (memberRepository.findByNickName(dto.getNickName()).isPresent()) {
            throw new DuplicateMemberNicknameException();
        }
    }

    @Transactional
    public SignUpResponse signUpAndRegisterProfile(SignUpRequest dto) throws IOException {
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateMemberException();
        }
        Member signUpMember = Member.createNewMember(dto);

        memberRepository.save(signUpMember);

        String token = jwtTokenProvider.createToken(signUpMember.getEmail());

        return SignUpResponse.builder()
                .jwtToken(token)
                .build();
    }

    @Transactional
    public EditProfileImageResponse editProfileImage(String profileImageUrl) throws IOException {
        Member member = securityUtil.getMemberByUserDetails();
        member.editProfileImageUrl(profileImageUrl);

        return EditProfileImageResponse.from(profileImageUrl);
    }

    public CalculateDayResponse calculateDayOfFirstMeet(Long petId) {
        Member member = securityUtil.getMemberByUserDetails();
        Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);

        long calculatedDay = LocalDateTimeUtils.calculateDaysFromNow(pet.getBirthDate());

        return CalculateDayResponse.of(member.getNickName(), pet.getPetName(), calculatedDay);
    }

    public CheckGroupResponse checkGroup() {
        Long memberId = securityUtil.getMemberIdByUserDetails();
        List<FamilyMember> familyByMemberId = memberQueryRepository.findFamilyByMemberId(memberId);
        if (familyByMemberId.isEmpty()) {
            throw new FamilyNotFoundException();
        }
        return CheckGroupResponse.builder()
                .groupId(familyByMemberId.get(0).getFamily().getFamilyId())
                .build();
    }

    public CheckPetResponse checkPet(Long familyId) {
        List<Pet> petByFamilyId = petQueryRepository.findPetsByFamilyId(familyId);
        if (petByFamilyId.isEmpty()) {
            throw new PetNotFoundException();
        }
        return CheckPetResponse.from(petByFamilyId);
    }

    @Transactional
    public void deleteMember() {
        Member member = securityUtil.getMemberByUserDetails();
        Family family = getFamily(member);
        int groupMembersCount = getFamilyMembers(family);

        if (canDeleteMember(member, groupMembersCount)) {
            memberRepository.delete(member);
        }

        if (canDeleteGroup(member, groupMembersCount)) {
            familyRepository.delete(family);
        }
    }

    private Boolean canDeleteMember(Member member, int groupMembersCount) {
        if (member.isFamilyLeader() && groupMembersCount > 1) {
            throw new CannotDeleteMemberException();
        }
        return true;
    }

    private Boolean canDeleteGroup(Member member, int groupMemberCount) {
        return member.isFamilyLeader() && groupMemberCount == 1;
    }

    private Family getFamily(Member member) {
        List<FamilyMember> familyByMemberId = memberQueryRepository.findFamilyByMemberId(member.getId());
        if (!familyByMemberId.isEmpty()) {
            return familyByMemberId.get(0).getFamily();
        }
        return null;
    }

    private int getFamilyMembers(Family family) {
        if(family == null)
            return 0;
        return familyQueryRepository.findMembersByFamilyId(family.getFamilyId()).size();
    }
}
