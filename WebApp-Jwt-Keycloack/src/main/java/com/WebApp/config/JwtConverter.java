package com.WebApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class JwtConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {

        Map<String, Object> resource_access = (Map<String, Object>) jwt.getClaims().get("resource_access");
        Map<String, ArrayList<String>> applicationRoles = (Map<String, ArrayList<String>>) resource_access.get("SpringBoot-Backend-1");
        ArrayList<String> roles = applicationRoles.get("roles");
        if (roles == null || roles.isEmpty()) {
            return new JwtAuthenticationToken(jwt, List.of());
        }
        Collection<SimpleGrantedAuthority> authorities =
                    roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());

            return new JwtAuthenticationToken(jwt, authorities);
        }
}
