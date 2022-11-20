package org.retriever.server.dailypet.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.dto.request.EditMemberProfileRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SignUpRequest;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.*;
import org.retriever.server.dailypet.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SignUpResponse> signUpAndRegisterProfile(@RequestBody @Valid SignUpRequest dto) throws IOException {
        return ResponseEntity.ok(memberService.signUpAndRegisterProfile(dto));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "회원 프로필 수정", description = "회원의 닉네임과 프로필 사진을 수정한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 프로필 정상 수정"),
            @ApiResponse(responseCode = "400", description = "회원 프로필 수정 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PatchMapping("/member/mypage/profile")
    public ResponseEntity<EditMemberProfileResponse> editMemberProfile(
            @RequestBody EditMemberProfileRequest dto) throws IOException {
        return ResponseEntity.ok(memberService.editMemberProfile(dto));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "메인 페이지 - 반려동물과 보낸 시간 조회", description = "나와 반려동물이 만난지 얼마나 됐는지 날짜를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/main/pets/{petId}/days")
    public ResponseEntity<CalculateDayResponse> calculateDayOfFirstMeet(@PathVariable Long petId) {
        return ResponseEntity.ok(memberService.calculateDayOfFirstMeet(petId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "가족 유무 조회", description = "해당 회원이 가족에 속해있는지 유무 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가족 정상 조회"),
            @ApiResponse(responseCode = "400", description = "가족에 속해 있지 않음"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/auth/family")
    public ResponseEntity<CheckGroupResponse> checkGroup() {
        return ResponseEntity.ok(memberService.checkGroup());
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "반려동물 유무 조회", description = "해당 회원의 가족이 반려동물을 등록했는지 유무 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려동물 정상 조회"),
            @ApiResponse(responseCode = "400", description = "반려동물이 등록되지 않음"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/auth/families/{familyId}/pet")
    public ResponseEntity<CheckPetResponse> checkPet(@PathVariable Long familyId) {
        return ResponseEntity.ok(memberService.checkPet(familyId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 정상 처리"),
            @ApiResponse(responseCode = "403", description = "그룹장인 경우 회원 탈퇴에 대한 권한이 없음"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @DeleteMapping("/auth/member")
    public ResponseEntity<Void> deleteMember() {
        memberService.deleteMember();
        return ResponseEntity.ok().build();
    }
}
