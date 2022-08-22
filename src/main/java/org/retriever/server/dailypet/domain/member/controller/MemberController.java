package org.retriever.server.dailypet.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.CalculateDayResponse;
import org.retriever.server.dailypet.domain.member.dto.response.EditProfileImageResponse;
import org.retriever.server.dailypet.domain.member.dto.response.SignUpResponse;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.domain.member.service.MemberService;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

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
            @ApiResponse(responseCode = "400", description = "다른 Sns 계정으로 이미 가입한 회원"),
            @ApiResponse(responseCode = "401", description = "새로운 회원으로 회원 등록 진행"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<SnsLoginResponse> checkMemberAndLogin(@RequestBody @Valid SnsLoginRequest dto) {
        SnsLoginResponse snsLoginResponse = memberService.checkMemberAndLogin(dto);
        return ResponseEntity.ok(snsLoginResponse);
    }

    @Operation(summary = "프로필 닉네임 검증", description = "전체 유저 기준 프로필 닉네임 중복 여부를 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임"),
            @ApiResponse(responseCode = "409", description = "사용 중인 중복 닉네임"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/validation/nickname")
    public ResponseEntity<Void> validateMemberNickName(@RequestBody @Valid ValidateMemberNicknameRequest dto) {
        memberService.validateMemberNickName(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 프로필 등록 (회원 가입)", description = "새로운 회원인 경우 프로필을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 프로필 정상 생성"),
            @ApiResponse(responseCode = "400", description = "회원 프로필 생성 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/auth/sign-up")
    public ResponseEntity<SignUpResponse> signUpAndRegisterProfile(@RequestBody @Valid SignUpRequest dto) {
        return ResponseEntity.ok(memberService.signUpAndRegisterProfile(dto));
    }

    @Operation(summary = "회원 프로필 사진 수정", description = "회원의 프로필 사진을 수정한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 프로필 사진 정상 수정"),
            @ApiResponse(responseCode = "400", description = "회원 프로필 사진 수정 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PatchMapping("/member/mypage/profile-image")
    public ResponseEntity<EditProfileImageResponse> editProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @RequestPart MultipartFile image) throws IOException {
        return ResponseEntity.ok(memberService.editProfileImage(userDetails, image));
    }

    @Operation(summary = "메인 페이지 - 반려동물과 보낸 시간 조회", description = "나와 반려동물이 만난지 얼마나 됐는지 날짜를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/main/pets/{petId}/days")
    public ResponseEntity<CalculateDayResponse> calculateDayOfFirstMeet(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable Long petId) {
        return ResponseEntity.ok(memberService.calculateDayOfFirstMeet(userDetails, petId));
    }


    @GetMapping("/test")
    public String test() {
        return "hello it's test";
    }
}
