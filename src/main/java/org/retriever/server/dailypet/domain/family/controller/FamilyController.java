package org.retriever.server.dailypet.domain.family.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.EnterFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyRoleNameRequest;
import org.retriever.server.dailypet.domain.family.dto.response.CreateFamilyResponse;
import org.retriever.server.dailypet.domain.family.dto.response.FindFamilyWithInvitationCodeResponse;
import org.retriever.server.dailypet.domain.family.service.FamilyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

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
    @PostMapping("/validation/families/{familyId}/family-role-name")
    public ResponseEntity<Void> validateFamilyRoleName(@PathVariable Long familyId, @RequestBody @Valid ValidateFamilyRoleNameRequest dto) {
        familyService.validateFamilyRoleName(familyId, dto);
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
    public ResponseEntity<CreateFamilyResponse> createFamily(@RequestBody @Valid CreateFamilyRequest dto) throws IOException {
        return ResponseEntity.ok(familyService.createFamily(dto));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "가족 초대 코드 입력", description = "가족을 찾기 위해 초대 코드를 입력하고 가족 정보를 반환한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가족 검색 성공"),
            @ApiResponse(responseCode = "400", description = "초대 코드에 해당하는 가족이 없음"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/families/{invitationCode}")
    public ResponseEntity<FindFamilyWithInvitationCodeResponse> findFamilyWithInvitationCode(@PathVariable String invitationCode) {
        return ResponseEntity.ok(familyService.findFamilyWithInvitationCode(invitationCode));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "가족 입장", description = "검증 완료된 가족 내 닉네임과 초대 코드를 통해 확인한 가족 그룹으로 입장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가족 입장 성공"),
            @ApiResponse(responseCode = "400", description = "가족 입장 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/families/{familyId}")
    public ResponseEntity<Void> enterFamily(
            @PathVariable Long familyId, @RequestBody @Valid EnterFamilyRequest dto) {
        familyService.enterFamily(familyId, dto);
        return ResponseEntity.ok().build();
    }
}
