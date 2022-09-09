package org.retriever.server.dailypet.global.utils.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SecurityUtilTest {

    @InjectMocks
    SecurityUtil securityUtil;

    @Mock
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("UserDetails를 통해서 인증된 유저 객체의 Id를 반환한다.")
    void getMemberIdByUserDetailsTest() {

        // given
        Authentication authentication = MemberFactory.createAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        // when
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // then
        Assertions.assertThat(securityUtil.getMemberIdByUserDetails()).isEqualTo(principal.getId());
    }

    @Test
    @DisplayName("UserDetails를 통해서 인증된 유저 객체를 반환한다.")
    void getMemberByUserDetailsTest() {

        // given
        Authentication authentication = MemberFactory.createAuthentication();
        Member member = MemberFactory.createTestMember();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // then
        assertEquals(member, securityUtil.getMemberByUserDetails());
    }

    @DisplayName("현재 인증된 멤버를 찾을 수 없는 경우 MemberNotFoundException 예외 발생")
    @Test
    void find_member_fail_with_authentication_and_throw_exception() {

        // given
        Authentication authentication = MemberFactory.createAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThrows(MemberNotFoundException.class, () -> securityUtil.getMemberByUserDetails());
    }
}
