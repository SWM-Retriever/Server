package org.retriever.server.dailypet.domain.oauth;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.Member;
import org.retriever.server.dailypet.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {

    private final MemberRepository memberRepository;

    public KakaoLoginResponse signInWithKakao(KakaoLoginRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(MemberNotFoundException::new);
        return new KakaoLoginResponse(member);
    }
}
