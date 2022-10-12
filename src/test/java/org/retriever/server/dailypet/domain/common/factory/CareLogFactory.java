package org.retriever.server.dailypet.domain.common.factory;

import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.retriever.server.dailypet.domain.petcare.enums.CareLogStatus;

import java.time.LocalDate;
import java.util.List;

public class CareLogFactory {

    private CareLogFactory() {

    }

    public static CareLog createTestCareLog() {
        return CareLog.builder()
                .logDate(LocalDate.now())
                .careLogStatus(CareLogStatus.CHECK)
                .build();
    }

    public static List<CareLog> createTestCareLogList(Member testMember, Member testMember2) {
        return List.of(CareLog.builder().member(testMember).build(), CareLog.builder().member(testMember2).build());
    }
}
