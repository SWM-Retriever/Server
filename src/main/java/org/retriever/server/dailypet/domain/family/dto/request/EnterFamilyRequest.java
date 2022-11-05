package org.retriever.server.dailypet.domain.family.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EnterFamilyRequest {

    private String familyRoleName;
}
