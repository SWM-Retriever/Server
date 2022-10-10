package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.retriever.server.dailypet.domain.pet.entity.Pet;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PetInfoResponse {
    private Long petId;

    private String petName;

    public PetInfoResponse(Pet pet) {
        this.petId = pet.getPetId();
        this.petName = pet.getPetName();
    }
}
