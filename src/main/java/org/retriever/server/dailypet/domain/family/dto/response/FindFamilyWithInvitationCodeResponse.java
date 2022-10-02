package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FindFamilyWithInvitationCodeResponse {

    private Long familyId;

    private String familyName;

    private int familyMemberCount;

    public static FindFamilyWithInvitationCodeResponse from(Family family) {
        return FindFamilyWithInvitationCodeResponse.builder()
                .familyId(family.getFamilyId())
                .familyName(family.getFamilyName())
                .familyMemberCount(family.getFamilyMemberList().size())
                .build();
    }
}
