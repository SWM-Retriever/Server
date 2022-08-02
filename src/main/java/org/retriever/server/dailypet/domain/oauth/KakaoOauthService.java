package org.retriever.server.dailypet.domain.oauth;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.user.User;
import org.retriever.server.dailypet.domain.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {

    private final UserRepository userRepository;

    public KakaoLoginResponse signInWithKakao(KakaoLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(UserNotFoundException::new);
        return new KakaoLoginResponse(user);
    }
}
