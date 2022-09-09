package org.retriever.server.dailypet.domain.family.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retriever.server.dailypet.domain.common.factory.FamilyFactory;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.EnterFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyRoleNameRequest;
import org.retriever.server.dailypet.domain.family.dto.response.CreateFamilyResponse;
import org.retriever.server.dailypet.domain.family.dto.response.FindFamilyWithInvitationCodeResponse;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyNameException;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyRoleNameException;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.enums.RoleType;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.global.utils.s3.S3FileUploader;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamilyServiceTest {

    @Mock
    FamilyRepository familyRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    S3FileUploader s3FileUploader;

    @Mock
    SecurityUtil securityUtil;

    @InjectMocks
    FamilyService familyService;

    @DisplayName("그룹 이름 검증 - 그룹 이름이 중복되는 경우 DuplicateFamilyNameException 발생")
    @Test
    void validate_family_name_fail_and_throw_exception() {

        // given
        Family family = FamilyFactory.createTestFamily();
        ValidateFamilyNameRequest validateFamilyNameRequest = FamilyFactory.createValidateFamilyNameRequest();
        when(familyRepository.findByFamilyName(any())).thenReturn(Optional.of(family));

        // when, then
        assertThrows(DuplicateFamilyNameException.class,
                () -> familyService.validateFamilyName(validateFamilyNameRequest));
    }

    @DisplayName("그룹 조회 - 초대코드를 이용해서 그룹 정보(그룹명, 그룹 인원수, 그룹 프로필 사진) 정상 조회")
    @Test
    void find_family_with_invitation_code_success() {

        // given
        Family family = FamilyFactory.createTestFamily();
        String invitationCode = "1234567890";
        when(familyRepository.findByInvitationCode(any())).thenReturn(Optional.of(family));

        // when
        FindFamilyWithInvitationCodeResponse response = familyService.findFamilyWithInvitationCode(invitationCode);

        // then
        assertThat(response.getFamilyId()).isEqualTo(family.getFamilyId());
        assertThat(response.getFamilyName()).isEqualTo(family.getFamilyName());
        assertThat(response.getProfileImageUrl()).isEqualTo(family.getProfileImageUrl());
        assertThat(response.getFamilyMemberCount()).isEqualTo(family.getFamilyMemberList().size());
    }

    @DisplayName("가족 조회 - 초대 코드와 일치하는 가족이 없을 경우 FamilyNotFoundException 발생")
    @Test
    void find_family_with_invitation_code_fail_and_throw_exception() {

        // given
        Family family = FamilyFactory.createTestFamily();
        String invitationCode = new StringBuilder(family.getInvitationCode()).reverse().toString();
        when(familyRepository.findByInvitationCode(any())).thenReturn(Optional.empty());

        // when, then
        assertThat(invitationCode).isNotEqualTo(family.getInvitationCode());
        assertThrows(FamilyNotFoundException.class,
                () -> familyService.findFamilyWithInvitationCode(invitationCode));
    }

    @DisplayName("가족 내 닉네임 검증 - 가족 내 닉네임 검증 실패 시 DuplicateFamilyRoleNameException 발생")
    @Test
    void validate_family_role_name_fail_and_throw_exception() {

        // given
        String name1 = "엄마";
        String name2 = "아빠";
        Member member = MemberFactory.createTestMemberWithFamilyMemberList(name1, name2);
        ValidateFamilyRoleNameRequest request = FamilyFactory.createValidateFamilyRoleNameRequest(name1);
        when(securityUtil.getMemberByUserDetails()).thenReturn(member);

        // when, then
        assertThrows(DuplicateFamilyRoleNameException.class, () -> familyService.validateFamilyRoleName(request));
    }

    @DisplayName("가족 생성 - 가족 정상 생성")
    @Test
    void create_family() throws IOException {

        // given
        Member member = MemberFactory.createTestMember();
        CreateFamilyRequest familyRequest = FamilyFactory.createFamilyRequest();
        MockMultipartFile image = MemberFactory.createMultipartFile();
        String imageURL = "testImageUrl";
        when(s3FileUploader.upload(any(), any())).thenReturn(imageURL);
        when(securityUtil.getMemberByUserDetails()).thenReturn(member);

        // when
        CreateFamilyResponse response = familyService.createFamily(familyRequest, image);

        // then
        assertThat(member.getFamilyRoleName()).isEqualTo(familyRequest.getFamilyRoleName());
        assertThat(member.getRoleType()).isEqualTo(RoleType.FAMILY_LEADER);
        assertThat(response.getFamilyName()).isEqualTo(familyRequest.getFamilyName());
        assertThat(response.getInvitationCode()).isNotNull();
        assertAll(
                () -> verify(securityUtil, times(1)).getMemberByUserDetails(),
                () -> verify(familyRepository, times(1)).save(any()),
                () -> verify(s3FileUploader, times(1)).upload(any(), any())
        );
    }

    @DisplayName("가족 입장 - 중복 검증이 완료된 가족 닉네임을 통해 해당 가족에 입장한다.")
    @Test
    void enter_family() {

        // given
        Member member = MemberFactory.createTestMember();
        Family family = FamilyFactory.createTestFamily();
        int size = family.getFamilyMemberList().size();
        EnterFamilyRequest request = FamilyFactory.createEnterFamilyRequest();
        when(familyRepository.findById(any())).thenReturn(Optional.of(family));
        when(securityUtil.getMemberByUserDetails()).thenReturn(member);

        // when
        familyService.enterFamily(family.getFamilyId(), request);

        // then
        assertThat(member.getFamilyRoleName()).isEqualTo(request.getFamilyRoleName());
        assertThat(family.getFamilyMemberList().size()).isEqualTo(size+1);
    }
}
