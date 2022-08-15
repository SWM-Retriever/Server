package org.retriever.server.dailypet.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.EditProfileImageResponse;
import org.retriever.server.dailypet.domain.member.dto.response.SignUpResponse;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.DifferentProviderTypeException;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberException;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberNicknameException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.retriever.server.dailypet.global.utils.s3.S3FileUploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3FileUploader s3FileUploader;

    public SnsLoginResponse checkMemberAndLogin(SnsLoginRequest dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if (!member.getProviderType().equals(dto.getProviderType())) {
            throw new DifferentProviderTypeException();
        }
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

        Member signUpMember = Member.createNewMember(dto);

        memberRepository.save(signUpMember);

        String token = jwtTokenProvider.createToken(signUpMember.getEmail());

        return SignUpResponse.builder()
                .jwtToken(token)
                .build();
    }

    @Transactional
    public EditProfileImageResponse editProfileImage(CustomUserDetails userDetails, MultipartFile image) throws IOException {
        Member member = memberRepository.findById(userDetails.getId()).orElseThrow(MemberNotFoundException::new);
        String profileImageUrl = s3FileUploader.upload(image, "test");

        member.editProfileImageUrl(profileImageUrl);

        return EditProfileImageResponse.from(profileImageUrl);
    }
}
