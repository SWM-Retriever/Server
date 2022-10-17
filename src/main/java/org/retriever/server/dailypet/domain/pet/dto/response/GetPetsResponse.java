package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetPetsResponse {

    List<PetInfoDetail> petInfoDetailList = new ArrayList<>();

    public static GetPetsResponse from(List<PetInfoDetail> petInfoDetailList) {
        return GetPetsResponse.builder()
                .petInfoDetailList(petInfoDetailList)
                .build();
    }
}
