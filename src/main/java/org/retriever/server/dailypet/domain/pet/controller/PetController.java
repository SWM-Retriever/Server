package org.retriever.server.dailypet.domain.pet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.dto.request.ValidatePetNameInFamilyRequest;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetCaresDetailResponse;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetKindListResponse;
import org.retriever.server.dailypet.domain.pet.dto.response.RegisterPetResponse;
import org.retriever.server.dailypet.domain.pet.enums.PetType;
import org.retriever.server.dailypet.domain.pet.service.PetService;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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
        petService.validatePetNameInFamily(dto, familyId);
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

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "반려 동물 등록", description = "가족에서 관리하는 반려동물을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려동물 등록 완료"),
            @ApiResponse(responseCode = "400", description = "반려동물 등록 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/families/{familyId}/pet")
    public ResponseEntity<RegisterPetResponse> registerPet(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestPart @Valid RegisterPetRequest dto,
                                                           @RequestPart MultipartFile image,
                                                           @PathVariable Long familyId) throws IOException {
        return ResponseEntity.ok(petService.registerPet(userDetails, dto, familyId, image));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "메인 페이지 - 반려동물 챙겨주기 탭 조회", description = "현재 반려동물의 챙겨주기 탭 항목과 현황을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "챙겨주기 조회 완료"),
            @ApiResponse(responseCode = "400", description = "챙겨주기 조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/pets/{petId}/cares/detail")
    public ResponseEntity<List<GetPetCaresDetailResponse>> getPetCareDetails(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @PathVariable Long petId) {
        return ResponseEntity.ok(petService.getPetCaresDetail(userDetails, petId));
    }
}
