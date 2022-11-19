package org.retriever.server.dailypet.domain.member.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.retriever.server.dailypet.domain.diary.entity.Diary;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.enums.AccountProgressStatus;
import org.retriever.server.dailypet.domain.member.enums.AccountStatus;
import org.retriever.server.dailypet.domain.member.enums.ProviderType;
import org.retriever.server.dailypet.domain.member.enums.RoleType;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Where(clause = "account_status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE member SET account_status = 'DELETED' WHERE member_id = ?")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    // 개인정보 이용 약관 (필수)
    private Boolean isPersonalInformationAgree;

    // 서비스 이용 약관 (필수)
    private Boolean isToSAgree;

    // 광고성 푸시알람 수신 동의 (선택)
    private Boolean isPushAgree;

    // 프로필 정보 추가 수집 동의 (선택)
    private Boolean isProfileInformationAgree;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickName;

    private String profileImageUrl;

    @Column(length = 50)
    private String familyRoleName;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    private AccountProgressStatus accountProgressStatus;

    private String deviceToken;

    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<FamilyMember> familyMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<CareLog> careLogList = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Diary> diaryList = new ArrayList<>();

    @Builder
    public Member(String email, String nickName, String profileImageUrl, ProviderType type, String deviceToken, Boolean isPushAgree, Boolean isProfileInformationAgree) {
        this.isPersonalInformationAgree = true;
        this.isToSAgree = true;
        this.isPushAgree = isPushAgree;
        this.isProfileInformationAgree = isProfileInformationAgree;
        this.accountStatus = AccountStatus.ACTIVE;
        this.email = email;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.familyRoleName = "별명을 입력해주세요!";
        this.providerType = type;
        this.roleType = RoleType.MEMBER;
        this.deviceToken = deviceToken;
        this.accountProgressStatus = AccountProgressStatus.PROFILE;
    }

    public static Member createNewMember(SignUpRequest signUpRequest) {
        return Member.builder()
                .email(signUpRequest.getEmail())
                .nickName(signUpRequest.getNickName())
                .profileImageUrl(signUpRequest.getProfileImageUrl())
                .providerType(signUpRequest.getProviderType())
                .deviceToken(signUpRequest.getDeviceToken())
                .isPushAgree((signUpRequest.getIsPushAgree()))
                .isProfileInformationAgree(signUpRequest.getIsProfileInformationAgree())
                .isPersonalInformationAgree(true)
                .isToSAgree(true)
                .accountStatus(AccountStatus.ACTIVE)
                .familyRoleName("별명을 입력해주세요!")
                .roleType(RoleType.MEMBER)
                .accountProgressStatus(AccountProgressStatus.PROFILE)
                .build();
    }

    public void createGroup(String familyRoleName) {
        setFamilyLeader();
        changeFamilyRoleName(familyRoleName);
        changeProgressStatusToGroup();
    }

    public void setFamilyLeader() {
        this.roleType = RoleType.FAMILY_LEADER;
    }

    public void setGroupMember() {
        this.roleType = RoleType.MEMBER;
    }

    public void changeFamilyRoleName(String familyRoleName) {
        this.familyRoleName = familyRoleName;
    }

    public void editProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void deleteMember() {
        // TODO : 리더일 경우 따로 처리
        if (this.roleType == RoleType.FAMILY_LEADER) {

        }
        this.accountStatus = AccountStatus.DELETED;
    }

    public void changeProgressStatusToGroup() {
        this.accountProgressStatus = AccountProgressStatus.GROUP;
    }

    public void changeProgressStatusToPet() {
        this.accountProgressStatus = AccountProgressStatus.PET;
    }

    public void createDiary(Diary diary) {
        this.diaryList.add(diary);
    }

    public boolean isFamilyLeader() {
        return this.roleType == RoleType.FAMILY_LEADER;
    }
}
