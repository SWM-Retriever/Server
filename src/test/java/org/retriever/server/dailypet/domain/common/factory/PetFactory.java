package org.retriever.server.dailypet.domain.common.factory;

import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.enums.Gender;
import org.retriever.server.dailypet.domain.pet.enums.PetType;

import java.time.LocalDate;

public class PetFactory {

    public static RegisterPetRequest createRegisterPetRequest() {
        return RegisterPetRequest.builder()
                .petName("petTest")
                .birthDate(LocalDate.of(2022, 8, 13))
                .gender(Gender.MALE)
                .registerNumber("12345678")
                .isNeutered(Boolean.TRUE)
                .petType(PetType.DOG)
                .weight(5.8)
                .petKindId(1L)
                .build();
    }
}
