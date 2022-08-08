package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class CreateFamilyResponse {

    private String familyName;

    private String invitationCode;
}
