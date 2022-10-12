package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetPetCaresResponse {

    Long petId;
    List<CurrentPetCareInfo> caresInfoList = new ArrayList<>();

    public static GetPetCaresResponse of(Long petId, List<CurrentPetCareInfo> caresInfoList) {
        return GetPetCaresResponse.builder()
                .petId(petId)
                .caresInfoList(caresInfoList)
                .build();
    }
}
