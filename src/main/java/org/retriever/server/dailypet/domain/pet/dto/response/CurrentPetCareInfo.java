package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.retriever.server.dailypet.domain.petcare.enums.CustomDayOfWeek;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CurrentPetCareInfo {

    private Long careId;

    private String careName;

    private int totalCareCount;

    private int currentCount;

    private List<CareLogHistory> checkList = new ArrayList<>();

    private List<CustomDayOfWeek> dayOfWeeks = new ArrayList<>();

    public static CurrentPetCareInfo of(PetCare petCare, List<CareLogHistory> list, List<CustomDayOfWeek> dayOfWeeks) {
        return CurrentPetCareInfo.builder()
                .careId(petCare.getPetCareId())
                .careName(petCare.getCareName())
                .currentCount(list.size())
                .totalCareCount(petCare.getTotalCountPerDay())
                .checkList(list)
                .dayOfWeeks(dayOfWeeks)
                .build();
    }
}
