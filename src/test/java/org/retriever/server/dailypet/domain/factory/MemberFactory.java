package org.retriever.server.dailypet.domain.factory;

import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.enums.ProviderType;

public class MemberFactory {

    private MemberFactory() {
    }

    public static Member createTestMember() {
        return Member.builder()
                .email("test@naver.com")
                .nickName("test")
                .profileImageUrl("abcdefghijk")
                .type(ProviderType.KAKAO)
                .deviceToken("abcdefg12345")
                .build();
    }

    public static SnsLoginRequest createSnsLoginRequest() {
        return SnsLoginRequest.builder()
                .snsNickName("test")
                .email("test@naver.com")
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
                .snsNickName("test")
                .deviceToken("abcde12345")
                .profileImageUrl("S3URL")
                .providerType(ProviderType.KAKAO)
                .build();
    }

}
