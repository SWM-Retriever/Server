package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.retriever.server.dailypet.domain.pet.entity.PetKind;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPetKindListResponse {

    private Long petKindId;

    private String petKindName;

    @Builder
    public GetPetKindListResponse(PetKind petKind) {
        this.petKindId = petKind.getPetKindId();
        this.petKindName = petKind.getPetKindName();
    }
}
