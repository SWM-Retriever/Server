package org.retriever.server.dailypet.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.report.dto.response.GetMyContributionResponse;
import org.retriever.server.dailypet.domain.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Report API")
public class ReportController {

    private final ReportService reportService;

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true)
    @Operation(summary = "메인 페이지 - 나의 기여도 조회", description = "이번 주 해당 반려동물에 대한 나의 기여도를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기여도 조회 완료"),
            @ApiResponse(responseCode = "400", description = "기여도 조회 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/main/contribution")
    public ResponseEntity<GetMyContributionResponse> getMyContribution(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(reportService.getMyContribution(startDate, endDate));
    }
}
