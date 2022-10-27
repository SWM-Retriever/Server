package org.retriever.server.dailypet.domain.petcare.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.pet.dto.response.CareLogHistory;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.retriever.server.dailypet.domain.petcare.enums.CustomDayOfWeek;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CheckPetCareResponse {

    private Long careId;

    private String careName;

    private int totalCareCount;

    private int currentCount;

    private List<CareLogHistory> checkList = new ArrayList<>();

    private List<CustomDayOfWeek> dayOfWeeks = new ArrayList<>();

    public static CheckPetCareResponse of(PetCare petCare, int afterCount, List<CareLogHistory> logHistoryList, List<CustomDayOfWeek> dayOfWeeks) {
        return CheckPetCareResponse.builder()
                .careId(petCare.getPetCareId())
                .careName(petCare.getCareName())
                .currentCount(afterCount)
                .checkList(logHistoryList)
                .dayOfWeeks(dayOfWeeks)
                .build();
    }
}
