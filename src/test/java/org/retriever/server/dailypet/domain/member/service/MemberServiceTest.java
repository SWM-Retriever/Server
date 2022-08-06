package org.retriever.server.dailypet.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retriever.server.dailypet.domain.factory.MemberFactory;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SignUpResponse;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberException;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberNicknameException;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    MemberService memberService;

    @DisplayName("로그인 - 가입된 회원인 경우 JWT 토큰을 반환해준다.")
    @Test
    void check_member_success_and_login() {

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
                () -> assertEquals(snsLoginResponse.getJwtToken(), testToken)
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
    void sign_up_success_and_register_profile() {

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
}
