package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetPetCaresDetailResponse {

    private String careName;

    private int currentCount;

    private int totalCountPerDay;

    private List<CareLogHistory> logHistoryList = new ArrayList<>();

    public static GetPetCaresDetailResponse of(PetCare petCare, List<CareLogHistory> list) {
        return GetPetCaresDetailResponse.builder()
                .careName(petCare.getCareName())
                .currentCount(petCare.getCurrentCount())
                .totalCountPerDay(petCare.getTotalCountPerDay())
                .logHistoryList(list)
                .build();
    }
}
