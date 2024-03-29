package org.retriever.server.dailypet.domain.common.config;

import org.retriever.server.dailypet.global.config.jwt.JwtAccessDeniedHandler;
import org.retriever.server.dailypet.global.config.jwt.JwtAuthenticationEntryPoint;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;
import org.springframework.boot.test.mock.mockito.MockBean;

public abstract class SecurityTestConfig {

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    protected JwtAccessDeniedHandler jwtAccessDeniedHandler;
}
