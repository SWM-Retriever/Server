package org.retriever.server.dailypet.domain.pet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.pet.dto.request.ValidatePetNameInFamilyRequest;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetKindListResponse;
import org.retriever.server.dailypet.domain.pet.enums.PetType;
import org.retriever.server.dailypet.domain.pet.service.PetService;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Pet API")
public class PetController {

    private final PetService petService;

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "가족 내 반려동물 이름 검증", description = "가족 내에서 반려동물의 닉네임 중복 여부를 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 반려 동물 이름"),
            @ApiResponse(responseCode = "409", description = "가족 내에서 사용 중인 중복 반려 동물 이름"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/validation/families/{familyId}/pet-name")
    public ResponseEntity<Void> validatePetNameInFamily(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody @Valid ValidatePetNameInFamilyRequest dto,
                                                        @PathVariable Long familyId) {
        petService.validatePetNameInFamily(userDetails, dto, familyId);
        return ResponseEntity.ok().build();
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "반려동물 품종 조회 ", description = "PetType(DOG, CAT)에 해당하는 품종을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려동물 품종 조회 성공"),
            @ApiResponse(responseCode = "400", description = "반려동물 품종 조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/pet/{petType}/kinds")
    public ResponseEntity<List<GetPetKindListResponse>> getPetKindListByType(
            @PathVariable PetType petType) {
        return ResponseEntity.ok(petService.getPetKindListByType(petType));
    }
}
