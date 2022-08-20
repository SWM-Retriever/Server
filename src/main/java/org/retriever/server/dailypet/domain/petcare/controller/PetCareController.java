package org.retriever.server.dailypet.domain.petcare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.service.PetCareService;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/pets/{petId}/pet-care")
    public ResponseEntity<Void> registerPetCare(@PathVariable Long petId, @RequestBody @Valid CreatePetCareRequest dto) {
        petCareService.registerPetCare(petId, dto);
        return ResponseEntity.ok().build();
    }
}
