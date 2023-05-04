package com.geekchow.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.junit.jupiter.api.Test;

import java.util.Locale;

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

    @Test
    public void MapTest() {
        /*
        what's going with stringFlux
        22:23:53.351 [Test worker] INFO reactor.Flux.Array.1 -- | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
        22:23:53.352 [Test worker] INFO reactor.Flux.Array.1 -- | request(unbounded)
        22:23:53.352 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Java)
        Java
        22:23:53.352 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Csharp)
        Csharp
        22:23:53.352 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Python)
        Python
        22:23:53.353 [Test worker] INFO reactor.Flux.Array.1 -- | onComplete()
        what's going with stringFlux1
        22:23:53.353 [Test worker] INFO reactor.Flux.Array.1 -- | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
        22:23:53.353 [Test worker] INFO reactor.Flux.MapFuseable.2 -- | onSubscribe([Fuseable] FluxMapFuseable.MapFuseableSubscriber)
        22:23:53.353 [Test worker] INFO reactor.Flux.MapFuseable.2 -- | request(unbounded)
        22:23:53.353 [Test worker] INFO reactor.Flux.Array.1 -- | request(unbounded)
        22:23:53.353 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Java)
        22:23:53.353 [Test worker] INFO reactor.Flux.MapFuseable.2 -- | onNext(JAVA)
        JAVA
        22:23:53.353 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Csharp)
        22:23:53.353 [Test worker] INFO reactor.Flux.MapFuseable.2 -- | onNext(CSHARP)
        CSHARP
        22:23:53.353 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Python)
        22:23:53.353 [Test worker] INFO reactor.Flux.MapFuseable.2 -- | onNext(PYTHON)
        PYTHON
        22:23:53.354 [Test worker] INFO reactor.Flux.Array.1 -- | onComplete()
        22:23:53.354 [Test worker] INFO reactor.Flux.MapFuseable.2 -- | onComplete()
         */
        Flux<String> stringFlux = Flux.just("Java", "Csharp", "Python").log();

        Flux<String> stringFlux1 = stringFlux.map((element) ->element.toUpperCase()).log();

        System.out.println("what's going with stringFlux");

        stringFlux.subscribe(System.out::println);

        System.out.println("what's going with stringFlux1");

        stringFlux1.subscribe(System.out::println);



    }

    @Test
    public void FlatMapTest() {
        /*
        22:22:04.488 [Test worker] INFO reactor.Flux.Array.1 -- | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
        22:22:04.489 [Test worker] INFO reactor.Flux.FlatMap.2 -- onSubscribe(FluxFlatMap.FlatMapMain)
        22:22:04.489 [Test worker] INFO reactor.Flux.FlatMap.2 -- request(unbounded)
        22:22:04.489 [Test worker] INFO reactor.Flux.Array.1 -- | request(256)
        22:22:04.490 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Java)
        22:22:04.490 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(java)
        java
        22:22:04.490 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(JAVA)
        JAVA
        22:22:04.490 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(Java Language)
        Java Language
        22:22:04.490 [Test worker] INFO reactor.Flux.Array.1 -- | request(1)
        22:22:04.490 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Csharp)
        22:22:04.491 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(csharp)
        csharp
        22:22:04.491 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(CSHARP)
        CSHARP
        22:22:04.491 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(Csharp Language)
        Csharp Language
        22:22:04.491 [Test worker] INFO reactor.Flux.Array.1 -- | request(1)
        22:22:04.491 [Test worker] INFO reactor.Flux.Array.1 -- | onNext(Python)
        22:22:04.491 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(python)
        python
        22:22:04.491 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(PYTHON)
        PYTHON
        22:22:04.491 [Test worker] INFO reactor.Flux.FlatMap.2 -- onNext(Python Language)
        Python Language
        22:22:04.491 [Test worker] INFO reactor.Flux.Array.1 -- | request(1)
        22:22:04.491 [Test worker] INFO reactor.Flux.Array.1 -- | onComplete()
        22:22:04.491 [Test worker] INFO reactor.Flux.FlatMap.2 -- onComplete()
         */
        Flux<String> stringFlux = Flux.just("Java", "Csharp", "Python").log();

        Flux<String> stringFlux1 = stringFlux.flatMap(
                value -> Flux.just(value.toLowerCase(Locale.ROOT), value.toUpperCase(Locale.ROOT), value + " Language")
        ).log();

        stringFlux1.subscribe(System.out::println);

    }
}