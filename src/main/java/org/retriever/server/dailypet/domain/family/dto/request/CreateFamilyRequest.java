package org.retriever.server.dailypet.domain.family.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class CreateFamilyRequest {

    private String familyName;

    private String familyRoleName;
}
