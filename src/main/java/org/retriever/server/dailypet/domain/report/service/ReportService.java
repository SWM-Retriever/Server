package org.retriever.server.dailypet.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.retriever.server.dailypet.domain.petcare.repository.CareLogQueryRepository;
import org.retriever.server.dailypet.domain.report.dto.response.GetMyContributionResponse;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SecurityUtil securityUtil;
    private final CareLogQueryRepository careLogQueryRepository;

    public GetMyContributionResponse getMyContribution(LocalDate startDate, LocalDate endDate) {
        Member member = securityUtil.getMemberByUserDetails();

        // 일주일 동안  기여한 횟수
        List<CareLog> careLogListBetweenDate = careLogQueryRepository.findCareLogBetweenDate(startDate, endDate);

        int totalCount = careLogListBetweenDate.size();
        int myCount = (int) careLogListBetweenDate.stream()
                .filter(
                        careLog -> member.getId().equals(careLog.getMember().getId()))
                .count();

        return GetMyContributionResponse.of(myCount, totalCount);
    }
}
