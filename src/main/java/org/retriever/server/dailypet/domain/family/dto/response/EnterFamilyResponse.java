package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.enums.GroupType;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.pet.dto.response.PetInfoResponse;

import java.util.List;
import java.util.stream.Collectors;

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

    private List<PetInfoResponse> petList;

    public static EnterFamilyResponse of(Member member, Family family) {
        return EnterFamilyResponse.builder()
                .familyId(family.getFamilyId())
                .familyName(family.getFamilyName())
                .nickName(member.getNickName())
                .groupType(family.getGroupType())
                .profileImageUrl(member.getProfileImageUrl())
                .petList(family.getPetList().stream()
                        .map(PetInfoResponse::new)
                        .collect(Collectors.toList()))
                .invitationCode(family.getInvitationCode())
                .build();
    }
}
