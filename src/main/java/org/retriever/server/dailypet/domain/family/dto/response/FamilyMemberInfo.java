package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.member.entity.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FamilyMemberInfo {

    private Long memberId;

    private String familyRoleName;

    private String profileImageUrl;

    public FamilyMemberInfo(FamilyMember familyMember) {
        this.memberId = familyMember.getMember().getId();
        this.familyRoleName = familyMember.getMember().getFamilyRoleName();
        this.profileImageUrl = familyMember.getMember().getProfileImageUrl();
    }
}
