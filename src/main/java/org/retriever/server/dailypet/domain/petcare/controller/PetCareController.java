package org.retriever.server.dailypet.domain.petcare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.dto.response.CancelPetCareResponse;
import org.retriever.server.dailypet.domain.petcare.dto.response.CheckPetCareResponse;
import org.retriever.server.dailypet.domain.petcare.service.PetCareService;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "PetCare API")
public class PetCareController {

    private final PetCareService petCareService;

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "챙겨주기 항목 등록", description = "해당 반려동물 기준으로 챙겨주기 항목을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "챙겨주기 항목 정상 등록"),
            @ApiResponse(responseCode = "400", description = "챙겨주기 항목 등록 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/pets/{petId}/care")
    public ResponseEntity<Void> registerPetCare(@PathVariable Long petId, @RequestBody @Valid CreatePetCareRequest dto) {
        petCareService.registerPetCare(petId, dto);
        return ResponseEntity.ok().build();
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "챙겨주기 항목 1회 체크", description = "해당 반려동물의 챙겨주기 항목을 체크한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "챙겨주기 정상 체크"),
            @ApiResponse(responseCode = "400", description = "챙겨주기 체크 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/pets/{petId}/cares/{careId}/check")
    public ResponseEntity<CheckPetCareResponse> checkPetCare(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable Long petId, @PathVariable Long careId) {
        return ResponseEntity.ok(petCareService.checkPetCare(userDetails, petId, careId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "챙겨주기 항목 1회 취소", description = "해당 반려동물의 챙겨주기 항목을 취소한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "챙겨주기 정상 취소"),
            @ApiResponse(responseCode = "400", description = "챙겨주기 취소 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/pets/{petId}/cares/{careId}/cancel")
    public ResponseEntity<CancelPetCareResponse> cancelPetCare(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable Long petId, @PathVariable Long careId) {
        return ResponseEntity.ok(petCareService.cancelPetCare(userDetails, petId, careId));
    }
}