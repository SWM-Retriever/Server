package org.retriever.server.dailypet.global.utils.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.retriever.server.dailypet.global.utils.security.exception.AuthenticationNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class SecurityUtil {

    private final MemberRepository memberRepository;

    public Member getMemberByUserDetails(CustomUserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationNotFoundException();
        }

        Long memberId = 0L;

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            memberId = principal.getId();
        }

        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
