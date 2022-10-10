package org.retriever.server.dailypet.domain.common.factory;

import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.*;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.enums.AccountProgressStatus;
import org.retriever.server.dailypet.domain.member.enums.AccountStatus;
import org.retriever.server.dailypet.domain.member.enums.ProviderType;
import org.retriever.server.dailypet.domain.member.enums.RoleType;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

public class MemberFactory {

    private MemberFactory() {
    }

    public static Member createTestMember() {
        return Member.builder()
                .id(1L)
                .email("test@naver.com")
                .nickName("test")
                .profileImageUrl("abcdefghijk")
                .providerType(ProviderType.KAKAO)
                .deviceToken("abcdefg12345")
                .roleType(RoleType.MEMBER)
                .isPushAgree(true)
                .isProfileInformationAgree(true)
                .isPersonalInformationAgree(true)
                .isToSAgree(true)
                .accountStatus(AccountStatus.ACTIVE)
                .accountProgressStatus(AccountProgressStatus.PROFILE)
                .familyRoleName("별명을 입력해주세요!")
                .build();
    }

    public static Member createTestMemberWithFamilyMemberList(String name1, String name2) {
        return Member.builder()
                .email("test@naver.com")
                .nickName("test")
                .profileImageUrl("abcdefghijk")
                .providerType(ProviderType.KAKAO)
                .deviceToken("abcdefg12345")
                .roleType(RoleType.MEMBER)
                .isPushAgree(true)
                .isProfileInformationAgree(true)
                .isPersonalInformationAgree(true)
                .isToSAgree(true)
                .accountStatus(AccountStatus.ACTIVE)
                .accountProgressStatus(AccountProgressStatus.GROUP)
                .familyRoleName("별명을 입력해주세요!")
                .familyMemberList(new ArrayList<>(List.of(
                        FamilyMember.builder().member(Member.builder().familyRoleName(name1).build()).build(),
                        FamilyMember.builder().member(Member.builder().familyRoleName(name2).build()).build()
                )))
                .build();
    }

    public static Member createTestMemberWithFamily(Long familyId) {
        return Member.builder()
                .email("test@naver.com")
                .nickName("test")
                .profileImageUrl("abcdefghijk")
                .providerType(ProviderType.KAKAO)
                .deviceToken("abcdefg12345")
                .roleType(RoleType.MEMBER)
                .isPushAgree(true)
                .isProfileInformationAgree(true)
                .isPersonalInformationAgree(true)
                .isToSAgree(true)
                .accountStatus(AccountStatus.ACTIVE)
                .accountProgressStatus(AccountProgressStatus.GROUP)
                .familyRoleName("별명을 입력해주세요!")
                .familyMemberList(new ArrayList<>(List.of(
                        FamilyMember.builder()
                                .member(Member.builder().familyRoleName("엄마").build())
                                .family(Family.builder().familyId(familyId).build())
                                .build()
                )))
                .build();
    }

    public static List<FamilyMember> createTestFamilyMember(Long familyId) {
        return List.of(FamilyMember.builder()
                .member(Member.builder().build())
                .family(Family.builder().familyId(familyId).build())
                .build());
    }

    public static SnsLoginRequest createSnsLoginRequest() {
        return SnsLoginRequest.builder()
                .snsNickName("test")
                .email("test@naver.com")
                .providerType(ProviderType.KAKAO)
                .build();
    }

    public static SnsLoginResponse createSnsLoginResponse() {
        return SnsLoginResponse.builder()
                .nickName("test")
                .email("test@naver.com")
                .jwtToken("testToken")
                .familyId(-1L)
                .familyName("testFamily")
                .invitationCode("testCode")
                .petIdList(List.of(-1L))
                .build();
    }

    public static String createToken(String token) {
        return token;
    }

    public static ValidateMemberNicknameRequest createValidateNicknameRequest(String nickname) {
        return ValidateMemberNicknameRequest.builder()
                .nickName(nickname)
                .build();
    }

    public static SignUpRequest createSignUpRequest() {
        return SignUpRequest.builder()
                .email("test@naver.com")
                .nickName("test")
                .deviceToken("abcde12345")
                .providerType(ProviderType.KAKAO)
                .isPushAgree(Boolean.TRUE)
                .isProfileInformationAgree(Boolean.TRUE)
                .build();
    }

    public static SignUpResponse createSignUpResponse() {
        return SignUpResponse.builder()
                .jwtToken("testToken")
                .build();
    }

    public static MockMultipartFile createMultipartFile() {
        return new MockMultipartFile(
                "testfile",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    public static Authentication createAuthentication() {
        Member member = createTestMember();
        CustomUserDetails userDetails = new CustomUserDetails(member);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public static CalculateDayResponse createCalculateMeetDay() {
        return CalculateDayResponse.builder()
                .petName("testPet")
                .userName("testUser")
                .calculatedDay(5)
                .build();
    }

    public static CheckGroupResponse createCheckGroupResponse() {
        return CheckGroupResponse.builder()
                .groupId(5L)
                .build();
    }

    public static CheckPetResponse createCheckPetResponse() {
        return CheckPetResponse.builder()
                .petIdList(List.of(1L, 2L, 3L))
                .build();
    }
}
