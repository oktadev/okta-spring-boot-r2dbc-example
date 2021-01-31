package com.okta.dev.oktar2dbc.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserDetails implements OidcUser {

    private final String email;
    private final OidcIdToken oidcIdToken;
    private final Map<String, Object> claims = new HashMap<>();
    private final Map<String, Object> attributes = new HashMap<>();

    public UserDetails(String email, OidcUserRequest oidcUserRequest) {
        this.email = email;
        this.claims.putAll(oidcUserRequest.getIdToken().getClaims());
        this.attributes.putAll(oidcUserRequest.getClientRegistration().getProviderDetails().getConfigurationMetadata());
        this.oidcIdToken = oidcUserRequest.getIdToken();
    }

    @Override
    public Map<String, Object> getClaims() {
        return new HashMap<>(claims);
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return new OidcUserInfo(getClaims());
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcIdToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>(attributes);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getName() {
        return email;
    }
}
