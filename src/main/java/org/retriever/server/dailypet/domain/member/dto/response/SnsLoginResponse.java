package org.retriever.server.dailypet.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class SnsLoginResponse {

    @Schema(description = "유저 프로필 이름")
    private String nickName;

    @Schema(description = "유저 SNS email")
    private String email;

    @Schema(description = "jwt Token")
    private String jwtToken;

    @Schema(description = "familyId")
    private Long familyId;

    private String familyName;

    private String invitationCode;

    @Schema(description = "petId List")
    private List<Long> petIdList = new ArrayList<>();
}
