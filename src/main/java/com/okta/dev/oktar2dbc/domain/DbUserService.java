package com.okta.dev.oktar2dbc.domain;

import com.okta.dev.oktar2dbc.database.UserEntity;
import com.okta.dev.oktar2dbc.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public class DbUserService implements OAuth2UserService<OidcUserRequest, OidcUser>, ReactiveUserDetailsService {
    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_EMAIL = "email";

    private final UserRepository userRepository;

    public DbUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {
        Map<String, Object> metadata = oidcUserRequest.getIdToken().getClaims();
        String email = (String) metadata.get(CLAIM_EMAIL);
        String name = (String) metadata.get(CLAIM_NAME);

        Flux<UserEntity> userLookup = userRepository.findByEmail(email);
        UserEntity userEntity = userLookup.blockFirst();

        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setEmail(email);
        }

        userEntity.setName(name);
        userEntity = userRepository.save(userEntity).block();

        return new UserDetails(userEntity.getEmail(), oidcUserRequest);
    }

    @Override
    public Mono<org.springframework.security.core.userdetails.UserDetails> findByUsername(String username) {
        return null;
    }
}
