package org.retriever.server.dailypet.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.response.AccountProgressStatusResponse;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.domain.member.service.LoginService;
import org.retriever.server.dailypet.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Login API")
public class LoginController {

    private final LoginService loginService;

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "서비스 등록 진행 상황 조회", description = "어느 단계까지 진행했는지 조회한다. PROFILE(0), GROUP(1), PET(2)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단계 조회 성공"),
            @ApiResponse(responseCode = "400", description = "단계 조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/progress-status")
    public ResponseEntity<AccountProgressStatusResponse> checkProgressStatus() {
        return ResponseEntity.ok(loginService.checkProgressStatus());
    }
}
