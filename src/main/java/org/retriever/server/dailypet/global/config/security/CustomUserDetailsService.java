package org.retriever.server.dailypet.global.config.security;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) {
        Member member = memberRepository.findById(Long.valueOf(userId)).orElseThrow(IllegalArgumentException::new);
        return new CustomUserDetails(member);
    }
}
