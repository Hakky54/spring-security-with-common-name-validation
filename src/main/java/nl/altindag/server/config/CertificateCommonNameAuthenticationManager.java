package nl.altindag.server.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

public class CertificateCommonNameAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {

    public CertificateCommonNameAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final String username = authentication.getName();
        return retrieveUser(username)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

}
