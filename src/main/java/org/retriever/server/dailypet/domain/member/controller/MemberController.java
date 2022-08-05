package org.retriever.server.dailypet.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Member API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "SNS 회원 조회", description = "회원 조회 후 기존 회원일 경우 로그인," +
            "새로운 회원일 경우 회원 등록 페이지로 이동")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기존 회원이므로 로그인 진행"),
            @ApiResponse(responseCode = "401", description = "새로운 회원으로 회원 등록 진행"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<SnsLoginResponse> checkMemberAndLogin(@RequestBody SnsLoginRequest dto) {
        SnsLoginResponse snsLoginResponse = memberService.checkMemberAndLogin(dto);
        return ResponseEntity.ok(snsLoginResponse);
    }

    @GetMapping("/test")
    public String test() {
        return "hello it's test";
    }
}
