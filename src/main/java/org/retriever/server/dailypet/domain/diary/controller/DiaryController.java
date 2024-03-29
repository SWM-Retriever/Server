package org.retriever.server.dailypet.domain.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.diary.dto.request.CreateDiaryRequest;
import org.retriever.server.dailypet.domain.diary.dto.request.EditDiaryRequest;
import org.retriever.server.dailypet.domain.diary.dto.response.DiaryView;
import org.retriever.server.dailypet.domain.diary.dto.response.GetGroupDiaryResponse;
import org.retriever.server.dailypet.domain.diary.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Diary API")
public class DiaryController {

    private final DiaryService diaryService;

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "일기 조회", description = "그룹 기준 반려 일기를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려 일기 조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/families/{familyId}/diaries")
    public ResponseEntity<GetGroupDiaryResponse> getGroupDiaries(@PathVariable Long familyId) {
        return ResponseEntity.ok(diaryService.getGroupDiaries(familyId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "최신 일기 조회", description = "최신 반려 일기 1개를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려 일기 조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/families/{familyId}/diaries/recent")
    public ResponseEntity<DiaryView> getRecentDiary(@PathVariable Long familyId) {
        return ResponseEntity.ok(diaryService.getRecentDiary(familyId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "일기 생성", description = "해당 그룹에 반려 일기를 작성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려 일기 작성 성공"),
            @ApiResponse(responseCode = "400", description = "작성 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/families/{familyId}/diary")
    public ResponseEntity<Void> createDiary(@PathVariable Long familyId, @RequestBody CreateDiaryRequest request) {
        diaryService.createDiary(familyId, request);
        return ResponseEntity.ok().build();
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "일기 수정", description = "해당 그룹에 반려 일기를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려 일기 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "삭제 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PatchMapping("/families/{familyId}/diaries/{diaryId}")
    public ResponseEntity<Void> editDiary(@PathVariable Long familyId,
                                          @PathVariable Long diaryId,
                                          @RequestBody EditDiaryRequest request) {
        diaryService.editDiary(familyId, diaryId, request);
        return ResponseEntity.ok().build();
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "일기 삭제", description = "해당 그룹에 반려 일기를 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반려 일기 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "삭제 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @DeleteMapping("/families/{familyId}/diaries/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long familyId, @PathVariable Long diaryId) {
        diaryService.deleteDiary(familyId, diaryId);
        return ResponseEntity.ok().build();
    }
}
