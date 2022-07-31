package org.retriever.server.dailypet.global.config.security;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.user.User;
import org.retriever.server.dailypet.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(IllegalArgumentException::new);
        return new CustomUserDetails(user);
    }
}
