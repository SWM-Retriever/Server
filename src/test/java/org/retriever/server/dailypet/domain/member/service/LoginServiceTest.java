package org.retriever.server.dailypet.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.family.dto.response.AccountProgressStatusResponse;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.enums.AccountProgressStatus;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    SecurityUtil securityUtil;

    @InjectMocks
    LoginService loginService;

    @Test
    @DisplayName("프로필 등록 후 이탈한 회원 조회")
    void get_progress_status_profile() {

        // given
        Member testMember = MemberFactory.createTestMember();
        given(securityUtil.getMemberByUserDetails()).willReturn(testMember);

        // when
        AccountProgressStatusResponse response = loginService.checkProgressStatus();

        // then
        assertThat(response.getStatus()).isEqualTo(AccountProgressStatus.PROFILE);
    }

    @Test
    @DisplayName("그룹 등록 후 이탈한 회원 조회")
    void get_progress_status_group() {

        // given
        Member testMember = MemberFactory.createTestMember();
        given(securityUtil.getMemberByUserDetails()).willReturn(testMember);
        testMember.changeProgressStatusToGroup();

        // when
        AccountProgressStatusResponse response = loginService.checkProgressStatus();

        // then
        assertThat(response.getStatus()).isEqualTo(AccountProgressStatus.GROUP);
    }

    @Test
    @DisplayName("반려동물 등록까지 완료한 회원 조회")
    void get_progress_status_pet() {

        // given
        Member testMember = MemberFactory.createTestMember();
        given(securityUtil.getMemberByUserDetails()).willReturn(testMember);
        testMember.changeProgressStatusToPet();

        // when
        AccountProgressStatusResponse response = loginService.checkProgressStatus();

        // then
        assertThat(response.getStatus()).isEqualTo(AccountProgressStatus.PET);
    }
}
