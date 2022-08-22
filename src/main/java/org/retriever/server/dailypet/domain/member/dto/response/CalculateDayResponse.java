package org.retriever.server.dailypet.domain.member.dto.response;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@AllArgsConstructor
public class CalculateDayResponse {

    private String userName;

    private String petName;

    private int calculatedDay;

    public static CalculateDayResponse of(String userName, String petName, int diff) {
        return CalculateDayResponse.builder()
                .userName(userName)
                .petName(petName)
                .calculatedDay(diff)
                .build();
    }
}
