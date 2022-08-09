package org.retriever.server.dailypet.domain.pet.dto.request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ValidatePetNameInFamilyRequest {

    private String petName;
}
