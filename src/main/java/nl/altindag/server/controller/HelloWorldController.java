package nl.altindag.server.controller;

import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@EnableWebFluxSecurity
@RestController
public class HelloWorldController {

    @GetMapping(path = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        return "Hello";
    }

    @GetMapping(value = "/hello", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> events() {
        return Flux.just("Hello");
    }

}
