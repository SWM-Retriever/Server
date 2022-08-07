package org.retriever.server.dailypet.domain.member.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.enums.RoleType;
import org.retriever.server.dailypet.domain.member.enums.AccountStatus;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.member.enums.ProviderType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    private Boolean isPersonalInformationAgree;

    private Boolean isToSAgree;

    private Boolean isPushAgree;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String nickName;

    private String profileImageUrl;

    @Column(length = 50)
    private String familyRoleName;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String deviceToken;

    private String password;

    @Builder
    public Member(String email, String nickName, String profileImageUrl, ProviderType type, String deviceToken) {
        this.isPersonalInformationAgree = true;
        this.isToSAgree = true;
        this.isPushAgree = false;
        this.accountStatus = AccountStatus.ACTIVE;
        this.email = email;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.familyRoleName = "별명을 입력해주세요!";
        this.providerType = type;
        this.roleType = RoleType.MEMBER;
        this.deviceToken = deviceToken;
    }

    public static Member createNewMember(SignUpRequest signUpRequest) {
        return Member.builder()
                .email(signUpRequest.getEmail())
                .nickName(signUpRequest.getSnsNickName())
                .profileImageUrl(signUpRequest.getProfileImageUrl())
                .type(signUpRequest.getProviderType())
                .deviceToken(signUpRequest.getDeviceToken())
                .build();
    }
}
