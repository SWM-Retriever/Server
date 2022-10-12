package org.retriever.server.dailypet.domain.petcare.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.pet.dto.response.CareLogHistory;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CancelPetCareResponse {

    private Long petCareId;

    private int currentCount;

    private List<CareLogHistory> checkList = new ArrayList<>();

    public static CancelPetCareResponse of(Long petCareId, int afterCount, List<CareLogHistory> logHistoryList) {
        return CancelPetCareResponse.builder()
                .petCareId(petCareId)
                .currentCount(afterCount)
                .checkList(logHistoryList)
                .build();
    }
}
