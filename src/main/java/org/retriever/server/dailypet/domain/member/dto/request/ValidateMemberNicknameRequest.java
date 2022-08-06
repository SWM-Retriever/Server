package org.retriever.server.dailypet.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Builder
public class ValidateMemberNicknameRequest {

    @NotEmpty
    @Size(max = 10)
    private String nickName;
}
