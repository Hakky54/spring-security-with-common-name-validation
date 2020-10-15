package nl.altindag.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.x509.SubjectDnX509PrincipalExtractor;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity serverHttpSecurity,
            ReactiveAuthenticationManager authenticationManager,
            X509PrincipalExtractor principalExtractor) {
        return serverHttpSecurity.authorizeExchange().anyExchange().authenticated()
                .and()
                .x509()
                .principalExtractor(principalExtractor)
                .authenticationManager(authenticationManager)
                .and()
                .build();
    }

    @Bean
    public X509PrincipalExtractor principalExtractor() {
        return new SubjectDnX509PrincipalExtractor();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService reactiveUserDetailsService) {
        return new CertificateCommonNameAuthenticationManager(reactiveUserDetailsService);
    }

    @Bean
    public MapReactiveUserDetailsService mapReactiveUserDetailsService() {
        UserDetails bob = User.withUsername("black-hole")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .password("")
                .build();

        return new MapReactiveUserDetailsService(bob);
    }

}
