package org.retriever.server.dailypet.domain.pet.dto.request;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UpdatePetInfoRequest {

    private String petName;

    private LocalDate birthDate;

    private Double weight;

    private Boolean isNeutered;

    private String registerNumber;

    private String profileImageUrl;
}
