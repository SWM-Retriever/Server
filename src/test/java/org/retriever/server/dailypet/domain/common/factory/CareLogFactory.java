package org.retriever.server.dailypet.domain.common.factory;

import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.retriever.server.dailypet.domain.petcare.enums.CareLogStatus;

import java.time.LocalDate;

public class CareLogFactory {

    private CareLogFactory() {

    }

    public static CareLog createTestCareLog() {
        return CareLog.builder()
                .logDate(LocalDate.now())
                .careLogStatus(CareLogStatus.CHECK)
                .build();
    }
}
