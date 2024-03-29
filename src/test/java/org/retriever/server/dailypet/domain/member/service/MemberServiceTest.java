package org.retriever.server.dailypet.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retriever.server.dailypet.domain.common.factory.FamilyFactory;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SignUpResponse;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberException;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberNicknameException;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberQueryRepository;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.pet.exception.PetNotFoundException;
import org.retriever.server.dailypet.domain.pet.repository.PetQueryRepository;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    MemberQueryRepository memberQueryRepository;
    @Mock
    SecurityUtil securityUtil;
    @Mock
    FamilyRepository familyRepository;
    @Mock
    PetQueryRepository petQueryRepository;

    @InjectMocks
    MemberService memberService;

    @DisplayName("로그인 - 가입된 회원인 경우 JWT 토큰과 가족과 펫이 등록되어 있지 않으면 각각 0L과 빈 리스트가 리턴된다.")
    @Test
    void check_member_success_and_login_and_not_register_group_pet() {

        // given
        SnsLoginRequest snsLoginRequest = MemberFactory.createSnsLoginRequest();
        Member member = MemberFactory.createTestMember();
        String testToken = "jwtToken";
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(jwtTokenProvider.createToken(any())).thenReturn(MemberFactory.createToken(testToken));

        // when
        SnsLoginResponse snsLoginResponse = memberService.checkMemberAndLogin(snsLoginRequest);

        // then
        assertAll(
                () -> assertEquals(snsLoginResponse.getEmail(), member.getEmail()),
                () -> assertEquals(snsLoginResponse.getNickName(), member.getNickName()),
                () -> assertEquals(snsLoginResponse.getJwtToken(), testToken),
                () -> assertEquals(snsLoginResponse.getFamilyId(), 0L),
                () -> assertNull(snsLoginResponse.getFamilyName()),
                () -> assertEquals(snsLoginResponse.getProfileImageUrl(), member.getProfileImageUrl())
        );
    }

    @DisplayName("로그인 - 가입된 회원인 경우 JWT 토큰과 가족이 등록되어 있는 경우 해당 familyId와 펫이 등록되어 있지 않으면 빈 리스트가 리턴된다.")
    @Test
    void check_member_success_and_login_and_register_family() {

        // given
        SnsLoginRequest snsLoginRequest = MemberFactory.createSnsLoginRequest();
        Long testFamilyId = 3L;
        String testFamilyName = "testFamily";
        Member member = MemberFactory.createTestMember();
        List<FamilyMember> testFamilyMember = MemberFactory.createTestFamilyMember(testFamilyId, testFamilyName);
        String testToken = "jwtToken";
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(memberQueryRepository.findFamilyByMemberId(member.getId())).thenReturn(testFamilyMember);
        when(jwtTokenProvider.createToken(any())).thenReturn(MemberFactory.createToken(testToken));

        // when
        SnsLoginResponse snsLoginResponse = memberService.checkMemberAndLogin(snsLoginRequest);

        // then
        assertAll(
                () -> assertEquals(snsLoginResponse.getEmail(), member.getEmail()),
                () -> assertEquals(snsLoginResponse.getNickName(), member.getNickName()),
                () -> assertEquals(snsLoginResponse.getJwtToken(), testToken),
                () -> assertEquals(snsLoginResponse.getFamilyId(), testFamilyId),
                () -> assertNotNull(snsLoginResponse.getFamilyName()),
                () -> assertEquals(snsLoginResponse.getProfileImageUrl(), member.getProfileImageUrl())
        );
    }

    @DisplayName("로그인 - 가입되지 않은 회원인 경우 MemberNotFoundException 예외 발생")
    @Test
    void check_member_fail_and_throw_exception() {

        // given request 만들고
        SnsLoginRequest snsLoginRequest = MemberFactory.createSnsLoginRequest();
        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

        // when, then
        assertThrows(MemberNotFoundException.class,
                () -> memberService.checkMemberAndLogin(snsLoginRequest));
    }

    @DisplayName("닉네임 검증 - 프로필 등록에서 닉네임 검증 실패 시 DuplicateMemberNicknameException 예외 발생")
    @Test
    void validate_nickname_fail_and_throw_exception() {

        // given
        Member member = MemberFactory.createTestMember();
        ValidateMemberNicknameRequest nicknameRequest = MemberFactory.createValidateNicknameRequest(member.getNickName());
        when(memberRepository.findByNickName(any())).thenReturn(Optional.of(member));

        // when, then
        assertThrows(DuplicateMemberNicknameException.class,
                () -> memberService.validateMemberNickName(nicknameRequest));
    }

    @DisplayName("회원 가입 - 정상 회원 가입 후 프로필 등록")
    @Test
    void sign_up_success_and_register_profile() throws IOException {

        // given
        Member member = MemberFactory.createTestMember();
        SignUpRequest signUpRequest = MemberFactory.createSignUpRequest();
        String testToken = "jwtToken";
        when(memberRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(member);
        when(jwtTokenProvider.createToken(any())).thenReturn(testToken);

        // when
        SignUpResponse signUpResponse = memberService.signUpAndRegisterProfile(signUpRequest);

        // then
        assertAll(
                () -> assertEquals(signUpResponse.getJwtToken(), testToken),
                () -> verify(memberRepository, times(1)).save(any()),
                () -> verify(jwtTokenProvider, times(1)).createToken(any())
        );
    }

    @DisplayName("회원 가입 - 가입 시 중복된 이메일이 존재할 경우 DuplicateMemberException 예외 발생")
    @Test
    void sign_up_fail_and_throw_exception() {

        // given
        Member member = MemberFactory.createTestMember();
        SignUpRequest signUpRequest = MemberFactory.createSignUpRequest();
        when(memberRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(member));

        // when, then
        assertThrows(DuplicateMemberException.class, () -> memberService.signUpAndRegisterProfile(signUpRequest));
    }

    @DisplayName("회원 가입 - 회원 가입 도중 프로필 등록만 이탈한 회원일 경우 그룹이 존재하지 않고 FamilyNotFoundException 예외 발생")
    @Test
    void check_family_fail_and_throw_exception() {

        // given
        given(memberQueryRepository.findFamilyByMemberId(any())).willReturn(new ArrayList<>());
        given(securityUtil.getMemberIdByUserDetails()).willReturn(1L);

        // when, then
        assertThrows(FamilyNotFoundException.class, () -> memberService.checkGroup());
    }

    @DisplayName("회원 가입 - 회원 가입 도중 그룹 등록만 이탈한 회원일 경우 반려동물이 존재하지 않고 PetNotFoundException 예외 발생")
    @Test
    void check_pet_fail_and_throw_exception() {

        // given
        given(petQueryRepository.findPetsByFamilyId(any())).willReturn(new ArrayList<>());

        // when, then
        assertThrows(PetNotFoundException.class, () -> memberService.checkPet(any()));
    }

    @DisplayName("회원 탈퇴 - 회원 탈퇴 진행 시 계정 status는 deleted로 변하고 그룹은 삭제되지 않는다.")
    @Test
    void delete_member_with_role_member() {

        // given
        Member member = MemberFactory.createTestMember();
        Family family = FamilyFactory.createTestFamily();
        FamilyMember familyMember = FamilyMember.createFamilyMember(member, family);

        given(securityUtil.getMemberByUserDetails()).willReturn(member);
        given(memberQueryRepository.findFamilyByMemberId(any())).willReturn(List.of(familyMember));

        // when
        memberService.deleteMember();

        // then
        verify(memberRepository,times(1)).delete(any());
    }

    @DisplayName("회원 탈퇴 - 회원 탈퇴 진행 시 그룹 리더일 경우 모두 삭제된다.")
    @Test
    void delete_member_with_group_leader() {

        // given
        Member member = MemberFactory.createTestMember();
        member.setFamilyLeader();
        Family family = FamilyFactory.createTestFamily();
        FamilyMember familyMember = FamilyMember.createFamilyMember(member, family);

        given(securityUtil.getMemberByUserDetails()).willReturn(member);
        given(memberQueryRepository.findFamilyByMemberId(any())).willReturn(List.of(familyMember));

        // when
        memberService.deleteMember();

        // then
        verify(memberRepository,times(1)).delete(any());
        verify(familyRepository, times(1)).delete(any());
    }

//    void init() {
//        // given
//        Member member = MemberFactory.createTestMember();
//        Family family = FamilyFactory.createTestFamily();
//        FamilyMember familyMember = FamilyMember.createFamilyMember(member, family);
//        Pet pet = PetFactory.createTestPetWithFamilyAndPetKind(family, PetFactory.createTestPetKind());
//        PetCareAlarm petCareAlarm = PetCareAlarm.from(CustomDayOfWeek.MON);
//        PetCare petCare = PetCareFactory.createTestPetCareWithPetAndAlarm(pet, petCareAlarm);
//        CareLog careLog = CareLog.of(member, pet, petCare, CareLogStatus.CHECK);
//
//        given(securityUtil.getMemberByUserDetails()).willReturn(member);
//        given(memberQueryRepository.findFamilyByMemberId(any())).willReturn(List.of(familyMember));
//
//        // when
//        memberService.deleteMember();
//        // then
//        assertThat(member.getAccountStatus()).isEqualTo(AccountStatus.DELETED);
//        assertThat(family.getFamilyStatus()).isEqualTo(FamilyStatus.DELETED);
//        assertThat(familyMember.getIsDeleted()).isEqualTo(IsDeleted.TRUE);
//        assertThat(pet.getPetStatus()).isEqualTo(PetStatus.DELETED);
//        assertThat(petCare.getIsDeleted()).isEqualTo(IsDeleted.TRUE);
//        assertThat(careLog.getIsDeleted()).isEqualTo(IsDeleted.TRUE);
//        assertThat(petCareAlarm.getIsDeleted()).isEqualTo(IsDeleted.TRUE);
//        verify(memberRepository, times(1)).delete(any());
//        verify(familyRepository, times(1)).delete(any());
//    }
}
