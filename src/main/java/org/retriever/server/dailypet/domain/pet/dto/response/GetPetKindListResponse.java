package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.retriever.server.dailypet.domain.pet.entity.PetKind;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPetKindListResponse {

    List<PetKindPair> petKindList = new ArrayList<>();

    public static GetPetKindListResponse from(List<PetKindPair> list) {
        return GetPetKindListResponse.builder()
                .petKindList(list)
                .build();
    }
}
