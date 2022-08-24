package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.Getter;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;

@Getter
public class CareLogHistory {

    private Long memberId;

    private String familyRoleName;

    public CareLogHistory(CareLog careLog) {
        this.memberId = careLog.getMember().getId();
        this.familyRoleName = careLog.getMember().getFamilyRoleName();
    }
}
