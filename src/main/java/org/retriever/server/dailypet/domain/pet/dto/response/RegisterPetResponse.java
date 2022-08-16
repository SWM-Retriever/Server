package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.member.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RegisterPetResponse {

    private Long familyId;

    private String familyName;

    private String familyRoleName;

    private List<PetInfoResponse> petList;

    private int petCount;

    private String invitationCode;

    public static RegisterPetResponse from(Member member, Family family) {
        return RegisterPetResponse.builder()
                .familyId(family.getFamilyId())
                .familyName(family.getFamilyName())
                .familyRoleName(member.getFamilyRoleName())
                .petList(family.getPetList().stream()
                        .map(PetInfoResponse::new)
                        .collect(Collectors.toList()))
                .petCount(family.getPetList().size())
                .invitationCode(family.getInvitationCode())
                .build();
    }
}