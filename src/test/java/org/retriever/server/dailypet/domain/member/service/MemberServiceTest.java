package org.retriever.server.dailypet.domain.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SignUpResponse;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.enums.AccountStatus;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberException;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberNicknameException;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberQueryRepository;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.pet.exception.PetNotFoundException;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;
import org.retriever.server.dailypet.global.utils.s3.S3FileUploader;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.web.multipart.MultipartFile;

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
    S3FileUploader s3FileUploader;
    @Mock
    MemberQueryRepository memberQueryRepository;
    @Mock
    SecurityUtil securityUtil;

    @InjectMocks
    MemberService memberService;

    @DisplayName("로그인 - 가입된 회원인 경우 JWT 토큰과 가족과 펫이 등록되어 있지 않으면 각각 -1이 리턴된다.")
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
                () -> assertEquals(snsLoginResponse.getSnsNickName(), member.getNickName()),
                () -> assertEquals(snsLoginResponse.getJwtToken(), testToken),
                () -> assertEquals(snsLoginResponse.getFamilyId(), -1L),
                () -> assertEquals(snsLoginResponse.getPetIdList(), List.of(-1L))
        );
    }

    @DisplayName("로그인 - 가입된 회원인 경우 JWT 토큰과 가족이 등록되어 있는 경우 해당 familyId와 펫이 등록되어 있지 않으면 -1이 리턴된다.")
    @Test
    void check_member_success_and_login_and_register_family() {

        // given
        SnsLoginRequest snsLoginRequest = MemberFactory.createSnsLoginRequest();
        Long testFamilyId = 3L;
        Member member = MemberFactory.createTestMember();
        List<FamilyMember> testFamilyMember = MemberFactory.createTestFamilyMember(testFamilyId);
        String testToken = "jwtToken";
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(memberQueryRepository.findFamilyByMemberId(member.getId())).thenReturn(testFamilyMember);
        when(jwtTokenProvider.createToken(any())).thenReturn(MemberFactory.createToken(testToken));

        // when
        SnsLoginResponse snsLoginResponse = memberService.checkMemberAndLogin(snsLoginRequest);

        // then
        assertAll(
                () -> assertEquals(snsLoginResponse.getEmail(), member.getEmail()),
                () -> assertEquals(snsLoginResponse.getSnsNickName(), member.getNickName()),
                () -> assertEquals(snsLoginResponse.getJwtToken(), testToken),
                () -> assertEquals(snsLoginResponse.getFamilyId(), testFamilyId),
                () -> assertEquals(snsLoginResponse.getPetIdList(), List.of(-1L))
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
        String imageURL = "testImageUrl";
        MultipartFile image = MemberFactory.createMultipartFile();
        when(memberRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(member);
        when(jwtTokenProvider.createToken(any())).thenReturn(testToken);
        when(s3FileUploader.upload(any(), any())).thenReturn(imageURL);

        // when
        SignUpResponse signUpResponse = memberService.signUpAndRegisterProfile(signUpRequest, image);

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
        MultipartFile image = MemberFactory.createMultipartFile();
        when(memberRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(member));

        // when, then
        assertThrows(DuplicateMemberException.class, () -> memberService.signUpAndRegisterProfile(signUpRequest, image));
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
        given(memberQueryRepository.findPetByFamilyId(any())).willReturn(new ArrayList<>());

        // when, then
        assertThrows(PetNotFoundException.class, () -> memberService.checkPet(any()));
    }

    @DisplayName("회원 탈퇴 - 회원 탈퇴 진행 시 계정 status는 deleted로 변한다")
    @Test
    void delete_member() {

        // given
        Member member = MemberFactory.createTestMember();
        given(securityUtil.getMemberByUserDetails()).willReturn(member);

        // when
        memberService.deleteMember();

        // then
        Assertions.assertThat(member.getAccountStatus()).isEqualTo(AccountStatus.DELETED);
    }
}
