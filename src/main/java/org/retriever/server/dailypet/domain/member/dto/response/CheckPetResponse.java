package org.retriever.server.dailypet.domain.member.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.pet.entity.Pet;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class CheckPetResponse {

    private List<Long> petIdList;

    public static CheckPetResponse from(List<Pet> petList) {
        return CheckPetResponse.builder()
                .petIdList(petList.stream().map(Pet::getPetId).collect(Collectors.toList()))
                .build();
    }
}
