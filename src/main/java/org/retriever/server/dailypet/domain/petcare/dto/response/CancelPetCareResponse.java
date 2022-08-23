package org.retriever.server.dailypet.domain.petcare.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CancelPetCareResponse {

    private Long petCareId;

    private int currentCount;
}
