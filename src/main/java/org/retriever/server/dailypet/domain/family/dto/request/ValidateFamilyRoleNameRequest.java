package org.retriever.server.dailypet.domain.family.dto.request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ValidateFamilyRoleNameRequest {

    private String familyRoleName;
}
