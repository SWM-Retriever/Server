package org.retriever.server.dailypet.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberNicknameException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public SnsLoginResponse checkMemberAndLogin(SnsLoginRequest dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()));

        return SnsLoginResponse.builder()
                .snsNickName(dto.getSnsNickName())
                .email(dto.getEmail())
                .jwtToken(token)
                .build();
    }

    public void validateMemberNickName(ValidateMemberNicknameRequest dto) {
        if (memberRepository.findByNickName(dto.getNickName()).isPresent()) {
            throw new DuplicateMemberNicknameException();
        }
    }
}
