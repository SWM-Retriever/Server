package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.enums.GroupType;
import org.retriever.server.dailypet.domain.member.entity.Member;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class EnterFamilyResponse {

    private Long familyId;

    private String familyName;

    private String nickName;

    private String invitationCode;

    private GroupType groupType;

    private String profileImageUrl;

    public static EnterFamilyResponse of(Member member, Family family) {
        return EnterFamilyResponse.builder()
                .familyId(family.getFamilyId())
                .familyName(family.getFamilyName())
                .nickName(member.getNickName())
                .groupType(family.getGroupType())
                .profileImageUrl(member.getProfileImageUrl())
                .invitationCode(family.getInvitationCode())
                .build();
    }
}
