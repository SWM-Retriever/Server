package org.retriever.server.dailypet.domain.member.dto.response;

import org.retriever.server.dailypet.domain.member.entity.Member;

public class KakaoLoginResponse {

    private String name;
    private String email;

    public KakaoLoginResponse(Member member) {
        this.name = member.getNickName();
        this.email = member.getEmail();
    }
}
