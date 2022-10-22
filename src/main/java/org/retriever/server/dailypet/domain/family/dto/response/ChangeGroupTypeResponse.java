package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.enums.GroupType;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ChangeGroupTypeResponse {

    private Long familyId;

    private String familyName;

    private String invitationCode;

    private GroupType groupType;

    public static ChangeGroupTypeResponse from(Family family) {
        return ChangeGroupTypeResponse.builder()
                .familyId(family.getFamilyId())
                .familyName(family.getFamilyName())
                .invitationCode(family.getInvitationCode())
                .groupType(family.getGroupType())
                .build();
    }
}
