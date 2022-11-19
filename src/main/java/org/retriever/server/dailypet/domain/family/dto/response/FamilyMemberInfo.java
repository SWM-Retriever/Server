package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FamilyMemberInfo {

    private Long memberId;

    private String nickName;

    private String profileImageUrl;

    public FamilyMemberInfo(FamilyMember familyMember) {
        this.memberId = familyMember.getMember().getId();
        this.nickName = familyMember.getMember().getNickName();
        this.profileImageUrl = familyMember.getMember().getProfileImageUrl();
    }
}
