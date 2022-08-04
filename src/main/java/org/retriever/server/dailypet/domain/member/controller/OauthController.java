package org.retriever.server.dailypet.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.KakaoLoginRequestDto;
import org.retriever.server.dailypet.domain.member.dto.response.KakaoLoginResponse;
import org.retriever.server.dailypet.domain.member.service.KakaoOauthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Member API")
public class OauthController {

    private final KakaoOauthService kakaoOauthService;

    @Operation(summary = "카카오 SNS 회원 조회", description = "회원 조회 후 기존 회원일 경우 로그인," +
            "새로운 회원일 경우 회원 등록 페이지로 이동")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기존 회원이므로 로그인 진행"),
            @ApiResponse(responseCode = "401", description = "새로운 회원으로 회원 등록 진행"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
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
