package org.retriever.server.dailypet.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
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

    @Operation(summary = "프로필 닉네임 검증", description = "전체 유저 기준 프로필 닉네임 중복 여부를 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임"),
            @ApiResponse(responseCode = "401", description = "사용 중인 중복 닉네임"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/validate/nickname")
    public ResponseEntity<Void> validateMemberNickName(@RequestBody ValidateMemberNicknameRequest dto) {
        memberService.validateMemberNickName(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public String test() {
        return "hello it's test";
    }
}
