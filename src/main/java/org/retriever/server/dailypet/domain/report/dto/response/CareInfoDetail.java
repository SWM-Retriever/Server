package org.retriever.server.dailypet.domain.report.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class CareInfoDetail {

    private String careName;
    private int totalCareCount;
    private int myCareCount;

    public static CareInfoDetail of(PetCare petCare, int myCareCount) {
        return CareInfoDetail.builder()
                .careName(petCare.getCareName())
                .totalCareCount(petCare.getTotalCountPerDay())
                .myCareCount(myCareCount)
                .build();
    }
}
