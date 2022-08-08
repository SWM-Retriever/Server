package org.retriever.server.dailypet.domain.family.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.service.FamilyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
