package org.retriever.server.dailypet.domain.member.entity;

import lombok.Getter;
import org.retriever.server.dailypet.domain.member.enums.RoleType;
import org.retriever.server.dailypet.domain.member.enums.AccountStatus;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.member.enums.ProviderType;

import javax.persistence.*;

@Entity
@Getter
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
}
