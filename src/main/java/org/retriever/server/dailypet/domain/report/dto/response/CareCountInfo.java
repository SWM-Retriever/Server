package org.retriever.server.dailypet.domain.report.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CareCountInfo {

    private String familyRoleName;

    private float careCount;

    public static CareCountInfo of(String familyRoleName, float careCount) {
        return CareCountInfo.builder()
                .familyRoleName(familyRoleName)
                .careCount(careCount)
                .build();
    }
}
