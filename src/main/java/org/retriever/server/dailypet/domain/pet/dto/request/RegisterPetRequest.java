package org.retriever.server.dailypet.domain.pet.dto.request;

import lombok.*;
import org.retriever.server.dailypet.domain.pet.enums.Gender;
import org.retriever.server.dailypet.domain.pet.enums.PetType;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RegisterPetRequest {

    private String petName;

    private PetType petType;

    private Gender gender;

    private LocalDate birthDate;

    private Long petKindId;

    private Double weight;

    private Boolean isNeutered;

    private String registerNumber;

    private String profileImageUrl;
}
