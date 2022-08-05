package org.retriever.server.dailypet.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SnsLoginResponse {

    @Schema(description = "유저 SNS 프로필 이름")
    private String snsNickName;

    @Schema(description = "유저 SNS email")
    private String email;

    @Schema(description = "jwt Token")
    private String jwtToken;
}
