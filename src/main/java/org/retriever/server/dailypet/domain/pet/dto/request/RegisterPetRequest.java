package org.retriever.server.dailypet.domain.pet.dto.request;

import lombok.*;
import org.retriever.server.dailypet.domain.pet.enums.Gender;
import org.retriever.server.dailypet.domain.pet.enums.PetType;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RegisterPetRequest {

    private String petName;

    private PetType petType;

    private String profileImageUrl;

    private Gender gender;

    private LocalDateTime birthDate;

    private Long petKindId;

    private Double weight;

    private Boolean isNeutered;

    private String registerNumber;
}
