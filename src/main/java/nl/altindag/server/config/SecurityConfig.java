package nl.altindag.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.SslInfo;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.cert.Certificate;
import java.util.Optional;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .x509(Customizer.withDefaults())
                .authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
                .addFilterAfter(SecurityConfig::printCertificateIfPresent, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    /**
     * Note: this filter is not required. It is just added to easily print the certificate details of the client
     *       after a successful authentication.
     */
    private static Mono<Void> printCertificateIfPresent(ServerWebExchange exchange, WebFilterChain chain) {
        Optional.of(exchange)
                .map(ServerWebExchange::getRequest)
                .map(ServerHttpRequest::getSslInfo)
                .map(SslInfo::getPeerCertificates)
                .flatMap(certificates -> Optional.ofNullable(certificates[0]))
                .map(Certificate::toString)
                .ifPresent(System.out::println);
        return Mono.empty();
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
