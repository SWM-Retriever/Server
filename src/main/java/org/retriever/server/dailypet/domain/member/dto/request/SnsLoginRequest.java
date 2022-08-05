package org.retriever.server.dailypet.domain.member.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class SnsLoginRequest {

    @NotEmpty
    @Size(max = 10)
    private String snsNickName;

    @NotEmpty
    @Email
    private String email;
}
