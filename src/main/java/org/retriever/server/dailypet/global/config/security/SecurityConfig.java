package org.retriever.server.dailypet.global.config.security;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.global.config.jwt.JwtAccessDeniedHandler;
import org.retriever.server.dailypet.global.config.jwt.JwtAuthenticationEntryPoint;
import org.retriever.server.dailypet.global.config.jwt.JwtAuthenticationFilter;
import org.retriever.server.dailypet.global.config.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                    .cors()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

                    .httpBasic().disable()
                    .csrf().disable()
                    .formLogin().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationFilter)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/auth/test").permitAll()
                    .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .httpFirewall(defaultHttpFirewall())
                .ignoring()
                .antMatchers(
                        "/h2-console/**"
                        , "/favicon.ico",
                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**"
                );
    }

    /*
     * Cors 설정
     * */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedOrigin("*");
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/api/**", corsConfig);
        return corsConfigSource;
    }
}
