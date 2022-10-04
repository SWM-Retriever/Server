package org.retriever.server.dailypet.domain.common.factory;

import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.retriever.server.dailypet.domain.petcare.enums.CustomDayOfWeek;

import java.util.List;

public class PetCareFactory {

    private PetCareFactory() {
    }

    public static PetCare createTestPetCare() {
        return PetCare.builder()
                .careName("testCare")
                .totalCountPerDay(5)
                .build();
    }

    public static CreatePetCareRequest createPetCareRequest() {
        return CreatePetCareRequest.builder()
                .careName("testCare")
                .totalCountPerDay(5)
                .dayOfWeeks(List.of(CustomDayOfWeek.MON))
                .build();
    }
}
