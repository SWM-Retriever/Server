package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.retriever.server.dailypet.domain.pet.entity.PetKind;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetKindPair {

    private Long petKindId;
    private String petKindName;

    public PetKindPair(PetKind petKind) {
        this.petKindId = petKind.getPetKindId();
        this.petKindName = petKind.getPetKindName();
    }
}
