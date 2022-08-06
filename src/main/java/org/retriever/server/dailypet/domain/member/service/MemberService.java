package org.retriever.server.dailypet.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SignUpResponse;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberException;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberNicknameException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public SnsLoginResponse checkMemberAndLogin(SnsLoginRequest dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        String token = jwtTokenProvider.createToken(dto.getEmail());

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

    @Transactional
    public SignUpResponse signUpAndRegisterProfile(SignUpRequest dto) {
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateMemberException();
        }

        Member signUpMember = Member.createNewMember(
                dto.getEmail(),
                dto.getSnsNickName(),
                dto.getProfileImageUrl(),
                dto.getProviderType(),
                dto.getDeviceToken()
        );

        memberRepository.save(signUpMember);

        String token = jwtTokenProvider.createToken(signUpMember.getEmail());

        return SignUpResponse.builder()
                .jwtToken(token)
                .build();
    }
}
