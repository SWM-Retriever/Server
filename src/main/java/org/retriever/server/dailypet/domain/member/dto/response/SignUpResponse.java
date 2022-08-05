package org.retriever.server.dailypet.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpResponse {

    private String jwtToken;
}
