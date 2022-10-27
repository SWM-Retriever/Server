package org.retriever.server.dailypet.domain.member.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.enums.GroupType;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.pet.dto.response.PetInfoResponse;
import org.retriever.server.dailypet.domain.pet.entity.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class SnsLoginResponse {

    private String nickName;

    private String email;

    private String jwtToken;

    private Long familyId;

    private String familyName;

    private String invitationCode;

    private GroupType groupType;

    private String profileImageUrl;

    public static SnsLoginResponse of(Member member, Family family, String token) {
        return SnsLoginResponse.builder()
                .nickName(member.getNickName())
                .email(member.getEmail())
                .jwtToken(token)
                .familyId(family.getFamilyId())
                .familyName(family.getFamilyName())
                .invitationCode(family.getInvitationCode())
                .groupType(family.getGroupType())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
