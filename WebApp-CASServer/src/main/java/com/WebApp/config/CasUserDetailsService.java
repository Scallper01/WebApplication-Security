package com.WebApp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CasUserDetailsService
        implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    private static final Logger logger = LoggerFactory.getLogger(CasUserDetailsService.class);

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) {

        var assertion = token.getAssertion();
        var attributes = assertion.getPrincipal().getAttributes();

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Adjust "roles" to whatever your CAS attribute name is
        List<String> roles = (List<String>) attributes.getOrDefault("roles", List.of());
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
        }

        // Always include FACTOR_CAS
        authorities.add(new SimpleGrantedAuthority("FACTOR_CAS"));

        return new User(token.getName(), "", authorities);
    }
}
