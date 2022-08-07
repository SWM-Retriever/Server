package org.retriever.server.dailypet.domain.member.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ValidateMemberNicknameRequest {

    @NotEmpty
    @Size(max = 10)
    private String nickName;
}
