package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyPageMemberInfo {

    private Long memberId;

    private String familyRoleName;

    private String profileImageUrl;

    private boolean isGroupLeader;

    public MyPageMemberInfo(FamilyMember familyMember) {
        this.memberId = familyMember.getMember().getId();
        this.familyRoleName = familyMember.getMember().getFamilyRoleName();
        this.profileImageUrl = familyMember.getMember().getProfileImageUrl();
        this.isGroupLeader = familyMember.getMember().isFamilyLeader();
    }
}
