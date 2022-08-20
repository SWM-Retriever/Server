package org.retriever.server.dailypet.domain.petcare.dto.request;

import lombok.*;
import org.retriever.server.dailypet.domain.petcare.enums.CustomDayOfWeek;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePetCareRequest {

    private String careName;

    private List<CustomDayOfWeek> dayOfWeeks;

    private int repeatCnt;
}
