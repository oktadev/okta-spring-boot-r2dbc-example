package com.okta.dev.oktar2dbc.config;

import com.okta.dev.oktar2dbc.database.UserRepository;
import com.okta.dev.oktar2dbc.domain.DbUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository userRepository) {
        return new DbUserService(userRepository);
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers("/", "/index").permitAll()
            .anyExchange().authenticated()
            .and()
            .oauth2Login()
            .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/protected"))
            .and().build();
    }
}
