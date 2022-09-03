package org.retriever.server.dailypet.domain.member.dto.response;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckGroupResponse {

    private Long groupId;
}
