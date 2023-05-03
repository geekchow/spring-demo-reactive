package com.geekchow.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.junit.jupiter.api.Test;

class MonoFluxTest {

    @Test
    public void MonoTest() {
        Mono<?> stringMono = Mono
                .just("Phil")
                .then(Mono.error(new RuntimeException("Intent to throw an exception")))
                .log();

        stringMono.subscribe(System.out::println, (error) -> System.out.println(error.getMessage()));
    }

    @Test
    public void FluxTest() {
        Flux<?> stringFlux = Flux
                .just("Phil", "Zhou", "Full", "Stack", "Programmer")
                .concatWithValues("AWS", "DevOps")
                .concatWith(Flux.error(new RuntimeException("Intent to throw an exception within Flux Stream")))
                .concatWithValues("TypeScript", "Python")
                .log();

        stringFlux.subscribe(System.out::println, (error) -> System.out.println(error.getMessage()));
    }
}