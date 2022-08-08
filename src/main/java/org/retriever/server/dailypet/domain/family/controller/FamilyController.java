package org.retriever.server.dailypet.domain.family.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyRoleNameRequest;
import org.retriever.server.dailypet.domain.family.service.FamilyService;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Family API")
public class FamilyController {

    private final FamilyService familyService;

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "가족 이름 검증", description = "전체 가족 기준 가족 이름 중복 여부를 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 가족 이름"),
            @ApiResponse(responseCode = "409", description = "사용 중인 중복 가족 이름"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/validation/family-name")
    public ResponseEntity<Void> validateFamilyName(@RequestBody @Valid ValidateFamilyNameRequest dto) {
        familyService.validateFamilyName(dto);
        return ResponseEntity.ok().build();
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "가족 내 닉네임 검증", description = "가족 내 구성원들끼리 닉네임 중복 여부를 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 가족 이름"),
            @ApiResponse(responseCode = "409", description = "사용 중인 중복 가족 이름"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/validation/family-role-name")
    public ResponseEntity<Void> validateFamilyRoleName(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestBody @Valid ValidateFamilyRoleNameRequest dto) {
        familyService.validateFamilyRoleName(userDetails, dto);
        return ResponseEntity.ok().build();
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "가족 생성", description = "새로운 가족을 생성하고 해당 멤버는 가족 리더가 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가족 생성"),
            @ApiResponse(responseCode = "400", description = "가족 생성 오류"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/family")
    public ResponseEntity<Void> createFamily(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestBody @Valid CreateFamilyRequest dto) {
        familyService.createFamily(userDetails, dto);
        return ResponseEntity.ok().build();
    }
}
