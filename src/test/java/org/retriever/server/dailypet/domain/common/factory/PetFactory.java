package org.retriever.server.dailypet.domain.common.factory;

import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.dto.request.ValidatePetNameInFamilyRequest;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.entity.PetKind;
import org.retriever.server.dailypet.domain.pet.enums.Gender;
import org.retriever.server.dailypet.domain.pet.enums.PetStatus;
import org.retriever.server.dailypet.domain.pet.enums.PetType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PetFactory {

    public static Pet createTestPet() {
        return Pet.builder()
                .petId(1L)
                .petName("petTest")
                .profileImageUrl("testUrl")
                .birthDate(LocalDate.of(2022, 8, 13))
                .weight(5.8)
                .registerNumber("12345678")
                .isNeutered(Boolean.TRUE)
                .gender(Gender.MALE)
                .status(PetStatus.ACTIVE)
                .build();
    }

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

    public static ValidatePetNameInFamilyRequest createValidatePetNameInFamilyRequest(String name) {
        return ValidatePetNameInFamilyRequest.builder()
                .petName(name)
                .build();
    }

    public static List<PetKind> createUnknownPetKind() {
        return new ArrayList<>(List.of(
                PetKind.builder()
                .petKindId(0L)
                .petType(PetType.UNKNOWN)
                .petKindName("Unknown")
                .information("품종을 모르는 경우입니다.")
                .build()
        ));
    }

    public static PetKind createTestPetKind() {
        return PetKind.builder()
                .petKindId(1L)
                .petType(PetType.DOG)
                .petKindName("리트리버")
                .information("리트리버")
                .build();
    }
}
