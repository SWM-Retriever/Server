package org.retriever.server.dailypet.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.response.AccountProgressStatusResponse;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final SecurityUtil securityUtil;

    public AccountProgressStatusResponse checkProgressStatus() {
        return AccountProgressStatusResponse.builder()
                .status(securityUtil.getMemberByUserDetails()
                        .getAccountProgressStatus())
                .build();
    }
}
