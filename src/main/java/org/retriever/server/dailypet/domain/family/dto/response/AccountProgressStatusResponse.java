package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.member.enums.AccountProgressStatus;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AccountProgressStatusResponse {

    private AccountProgressStatus status;
}
