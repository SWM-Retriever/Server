package org.retriever.server.dailypet.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class SnsLoginResponse {

    @Schema(description = "유저 SNS 프로필 이름")
    private String snsNickName;

    @Schema(description = "유저 SNS email")
    private String email;

    @Schema(description = "jwt Token")
    private String jwtToken;
}
