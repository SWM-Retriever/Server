package org.retriever.server.dailypet.domain.petcare.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.pet.dto.response.CareLogHistory;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CheckPetCareResponse {

    private Long petCareId;

    private int currentCount;

    private List<CareLogHistory> checkList = new ArrayList<>();

    public static CheckPetCareResponse of(Long petCareId, List<CareLogHistory> logHistoryList) {
        return CheckPetCareResponse.builder()
                .petCareId(petCareId)
                .currentCount(logHistoryList.size())
                .checkList(logHistoryList)
                .build();
    }
}
