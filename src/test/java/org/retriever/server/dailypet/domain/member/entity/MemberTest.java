package org.retriever.server.dailypet.domain.member.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.enums.AccountStatus;
import org.retriever.server.dailypet.domain.member.enums.RoleType;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("회원가입 request dto를 받아서 새로운 유저 생성")
    @Test
    void create_new_member() {

        // given
        SignUpRequest signUpRequest = MemberFactory.createSignUpRequest();

        // when
        Member newMember = Member.createNewMember(signUpRequest);

        // then
        assertThat(newMember.getId()).isNull();
        assertThat(newMember.getNickName()).isEqualTo(signUpRequest.getSnsNickName());
        assertThat(newMember.getEmail()).isEqualTo(signUpRequest.getEmail());
        assertThat(newMember.getProfileImageUrl()).isEqualTo(signUpRequest.getProfileImageUrl());
        assertThat(newMember.getProviderType()).isEqualTo(signUpRequest.getProviderType());
        assertThat(newMember.getDeviceToken()).isEqualTo(signUpRequest.getDeviceToken());

        assertThat(newMember.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(newMember.getRoleType()).isEqualTo(RoleType.MEMBER);
        assertThat(newMember.getIsPersonalInformationAgree()).isTrue();
        assertThat(newMember.getIsToSAgree()).isTrue();
        assertThat(newMember.getIsPushAgree()).isFalse();

        assertThat(newMember.getFamilyRoleName()).isEqualTo("별명을 입력해주세요!");
        assertThat(newMember.getPassword()).isNull();
    }

    @DisplayName("멤버 가족 리더 권한으로 변경")
    @Test
    void change_family_leader() {

        // given
        Member testMember = MemberFactory.createTestMember();

        // when
        testMember.setFamilyLeader();

        // then
        assertThat(testMember.getRoleType()).isEqualTo(RoleType.FAMILY_LEADER);
    }
}
