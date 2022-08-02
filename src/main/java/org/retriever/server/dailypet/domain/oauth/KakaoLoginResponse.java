package org.retriever.server.dailypet.domain.oauth;

import org.retriever.server.dailypet.domain.user.User;

public class KakaoLoginResponse {

    private String name;
    private String email;

    public KakaoLoginResponse(User user) {
        this.name = user.getNickName();
        this.email = user.getEmail();
    }
}
