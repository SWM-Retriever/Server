package org.retriever.server.dailypet.domain.pet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.dto.request.UpdatePetInfoRequest;
import org.retriever.server.dailypet.domain.pet.dto.request.ValidatePetNameInFamilyRequest;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetKindListResponse;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetsResponse;
import org.retriever.server.dailypet.domain.pet.dto.response.PetInfoDetail;
import org.retriever.server.dailypet.domain.pet.dto.response.RegisterPetResponse;
import org.retriever.server.dailypet.domain.pet.enums.PetType;
import org.retriever.server.dailypet.domain.pet.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

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
    public ResponseEntity<Void> validatePetNameInFamily(@RequestBody @Valid ValidatePetNameInFamilyRequest dto,
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
    public ResponseEntity<GetPetKindListResponse> getPetKindListByType(
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
    public ResponseEntity<RegisterPetResponse> registerPet(@RequestBody @Valid RegisterPetRequest dto, @PathVariable Long familyId) throws IOException {
        return ResponseEntity.ok(petService.registerPet(dto, familyId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "반려 동물 전체 조회", description = "가족에서 관리하는 반려동물 정보를 전체 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려동물 조회 완료"),
            @ApiResponse(responseCode = "400", description = "반려동물 조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/families/{familyId}/pets")
    public ResponseEntity<GetPetsResponse> getPetsByFamilyId(@PathVariable Long familyId) throws IOException {
        return ResponseEntity.ok(petService.getPetsByFamilyId(familyId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "반려 동물 단일 조회", description = "가족에서 관리하는 반려동물을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려동물 조회 완료"),
            @ApiResponse(responseCode = "400", description = "반려동물 조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/families/{familyId}/pets/{petId}")
    public ResponseEntity<PetInfoDetail> getPetInfo(@PathVariable Long petId) throws IOException {
        return ResponseEntity.ok(petService.getPetInfo(petId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "반려 동물 수정", description = "가족에서 관리하는 반려동물 정보를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려동물 수정 완료"),
            @ApiResponse(responseCode = "400", description = "반려동물 수정 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PatchMapping("/families/{familyId}/pets/{petId}")
    public ResponseEntity<PetInfoDetail> updatePetInfo(@RequestBody UpdatePetInfoRequest updatePetInfoRequest, @PathVariable Long petId) throws IOException {
        return ResponseEntity.ok(petService.updatePetInfo(updatePetInfoRequest, petId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "반려 동물 삭제", description = "가족에서 관리하는 반려동물을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려동물 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "반려동물 삭제 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @DeleteMapping("/families/{familyId}/pets/{petId}")
    public ResponseEntity<Void> deletePetInfo(@PathVariable Long petId) throws IOException {
        petService.deletePetInfo(petId);
        return ResponseEntity.ok().build();
    }
}
