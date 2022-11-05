package org.retriever.server.dailypet.domain.family.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ValidateFamilyNameRequest {

    private String familyName;
}
