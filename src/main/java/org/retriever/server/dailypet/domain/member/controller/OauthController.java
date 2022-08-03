package org.retriever.server.dailypet.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.KakaoLoginRequestDto;
import org.retriever.server.dailypet.domain.member.dto.response.KakaoLoginResponse;
import org.retriever.server.dailypet.domain.member.service.KakaoOauthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class OauthController {

    private final KakaoOauthService kakaoOauthService;

    @PostMapping("/login")
    public ResponseEntity<KakaoLoginResponse> signInWithKakao(@RequestBody KakaoLoginRequestDto dto) {
        KakaoLoginResponse kakaoLoginResponse = kakaoOauthService.signInWithKakao(dto);
        return ResponseEntity.ok(kakaoLoginResponse);
    }

    @GetMapping("/test")
    public String test() {
        return "hello it's test";
    }
}
