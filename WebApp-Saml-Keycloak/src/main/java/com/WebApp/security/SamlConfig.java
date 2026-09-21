package com.WebApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class SamlConfig {

    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() throws Exception {
        // 1. Load your idp.crt certificate from src/main/resources/
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(
                new ClassPathResource("idp.crt").getInputStream()
        );

        Saml2X509Credential verificationCredential = Saml2X509Credential.verification(certificate);

        // 2. Register your Keycloak configuration explicitly in code
        RelyingPartyRegistration registration = RelyingPartyRegistration
                .withRegistrationId("WebApp-Saml-Keycloak") // Must match what your SecurityFilterChain expects
                .entityId("WebApp-Saml-Keycloak")
                .assertionConsumerServiceLocation("{baseUrl}/login/saml2/sso/{registrationId}")
                .assertingPartyMetadata(party -> party
                        .entityId("http://localhost:8080/realms/WebApp-Saml-Keycloak")
                        .singleSignOnServiceLocation("http://localhost:8080/realms/WebApp-Saml-Keycloak/protocol/saml")
                        .verificationX509Credentials(c -> c.add(verificationCredential))
                        .wantAuthnRequestsSigned(false)
                )
                .build();

        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }
}