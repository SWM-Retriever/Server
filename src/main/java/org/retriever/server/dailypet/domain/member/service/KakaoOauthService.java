package org.retriever.server.dailypet.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.dto.request.KakaoLoginRequestDto;
import org.retriever.server.dailypet.domain.member.dto.response.KakaoLoginResponse;
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
