package org.retriever.server.dailypet.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.retriever.server.dailypet.domain.member.enums.ProviderType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Builder
public class SignUpRequest {

    @NotEmpty
    @Size(max = 20)
    private String nickName;

    @NotEmpty
    @Email
    private String email;

    private ProviderType providerType;

    private String deviceToken;

    private Boolean isPushAgree;

    private Boolean isProfileInformationAgree;

    private String profileImageUrl;
}
