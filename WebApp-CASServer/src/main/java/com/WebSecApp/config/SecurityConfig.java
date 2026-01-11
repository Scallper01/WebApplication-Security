package com.WebSecApp.config;

import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.client.validation.Cas30ServiceTicketValidator;
import org.apereo.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/*
    /CAS Configuration :
        - ServiceProperties
        - CasAuthenticationEntryPoint
        - CasAuthenticationFiltrer
        - CasAuthenticationProvider

    Spring Security Configuration :
        - SecurityChain
*/

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${cas.server.url}")
    private String casServerUrl;
    @Value("${cas.service.url}")
    private String casServiceUrl;

    private final CasUserDetailsService casUserDetailsService;

    public SecurityConfig(CasUserDetailsService casUserDetailsService) {
        this.casUserDetailsService = casUserDetailsService;
    }

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties sp = new ServiceProperties();
        sp.setService(casServiceUrl);
        sp.setSendRenew(false);
        return sp;
    }

    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint(ServiceProperties serviceProperties){
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casServerUrl+"/login");
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties);
        return casAuthenticationEntryPoint;
    };


    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(CasAuthenticationProvider casAuthenticationProvider) {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setAuthenticationManager(new ProviderManager(casAuthenticationProvider));
        filter.setFilterProcessesUrl("/login/cas");
        return filter;
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(CasUserDetailsService userDetailsService,
                                                               ServiceProperties serviceProperties,
                                                               TicketValidator ticketValidator) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setAuthenticationUserDetailsService(userDetailsService);
        provider.setServiceProperties(serviceProperties);
        provider.setTicketValidator(ticketValidator);
        provider.setKey("key");
        return provider;
    }

    @Bean
    public TicketValidator cas30ServiceTicketValidator(){
        return new Cas30ServiceTicketValidator(this.casServerUrl);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CasAuthenticationFilter casAuthenticationFilter,
                                           CasAuthenticationEntryPoint casAuthenticationEntryPoint) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(casAuthenticationEntryPoint))
                .authorizeHttpRequests(auth ->
                                auth.requestMatchers("/login/cas").permitAll()
                                    .requestMatchers("/debug/auth").hasRole("ADMIN")
                                    .anyRequest().authenticated())
                .addFilterBefore(casAuthenticationFilter, CasAuthenticationFilter.class);
        return http.build();
    }


}
