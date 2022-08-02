package org.retriever.server.dailypet.domain.oauth;

import lombok.RequiredArgsConstructor;
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
