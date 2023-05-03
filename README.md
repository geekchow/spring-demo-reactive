# Read Me First
The following was discovered as part of building this project:

* The JVM level was changed from '11' to '17', review the [JDK Version Range](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions#jdk-version-range) on the wiki for more details.

# Getting Started

## Mono Stream Behaviour

### Plain Mono Stream
Only one element in the stream, onNext is executed once, then the whole stream is completed.
```java
    @Test
    public void MonoTest() {
        Mono<?> stringMono = Mono
                .just("Phil")
//                .then(Mono.error(new RuntimeException("Intent to throw an exception")))
                .log();

        stringMono.subscribe(System.out::println, (error) -> System.out.println(error.getMessage()));
    }
```

Output is as below
```shell
21:37:48.753 [Test worker] DEBUG reactor.util.Loggers -- Using Slf4j logging framework
21:37:48.759 [Test worker] INFO reactor.Mono.Just.1 -- | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription)
21:37:48.760 [Test worker] INFO reactor.Mono.Just.1 -- | request(unbounded)
21:37:48.760 [Test worker] INFO reactor.Mono.Just.1 -- | onNext(Phil)
Phil
21:37:48.760 [Test worker] INFO reactor.Mono.Just.1 -- | onComplete()
```

### Mono Stream with error

Error interrupt the whole Mono stream, subscriber can't accept the only one element at all.
```java
    @Test
    public void MonoTest() {
        Mono<?> stringMono = Mono
                .just("Phil")
                .then(Mono.error(new RuntimeException("Intent to throw an exception")))
                .log();

        stringMono.subscribe(System.out::println, (error) -> System.out.println(error.getMessage()));
    }
```

```shell
21:40:41.217 [Test worker] DEBUG reactor.util.Loggers -- Using Slf4j logging framework
21:40:41.223 [Test worker] INFO reactor.Mono.IgnoreThen.1 -- onSubscribe(MonoIgnoreThen.ThenIgnoreMain)
21:40:41.223 [Test worker] INFO reactor.Mono.IgnoreThen.1 -- request(unbounded)
21:40:41.224 [Test worker] ERROR reactor.Mono.IgnoreThen.1 -- onError(java.lang.RuntimeException: Intent to throw an exception)
21:40:41.224 [Test worker] ERROR reactor.Mono.IgnoreThen.1 -- 
java.lang.RuntimeException: Intent to throw an exception
	at com.geekchow.webflux.MonoFluxTest.MonoTest(MonoFluxTest.java:12)
	...
Intent to throw an exception
```


## Flux Stream Behaviour

### Plain Flux Stream

All elements within the Flux stream are accepted by subscriber one by one, and at last an onCompleted event occurs.

```java
    @Test
    public void FluxTest() {
        Flux<?> stringFlux = Flux
                .just("Phil", "Zhou", "Full", "Stack", "Programmer")
                .concatWithValues("AWS", "DevOps")
                .log();

        stringFlux.subscribe(System.out::println, (error) -> System.out.println(error.getMessage()));
    }
```

Output of plain flux stream.

```shell
21:43:59.734 [Test worker] DEBUG reactor.util.Loggers -- Using Slf4j logging framework
21:43:59.741 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onSubscribe(FluxConcatArray.ConcatArraySubscriber)
21:43:59.742 [Test worker] INFO reactor.Flux.ConcatArray.1 -- request(unbounded)
21:43:59.742 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Phil)
Phil
21:43:59.742 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Zhou)
Zhou
21:43:59.742 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Full)
Full
21:43:59.742 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Stack)
Stack
21:43:59.742 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Programmer)
Programmer
21:43:59.743 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(AWS)
AWS
21:43:59.743 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(DevOps)
DevOps
21:43:59.743 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onComplete()
```

### Flux stream with error element

Error element interrupt the Flux stream, but doesn't affect the elements before error.

```java
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
```


```shell
21:48:14.628 [Test worker] DEBUG reactor.util.Loggers -- Using Slf4j logging framework
21:48:14.635 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onSubscribe(FluxConcatArray.ConcatArraySubscriber)
21:48:14.636 [Test worker] INFO reactor.Flux.ConcatArray.1 -- request(unbounded)
21:48:14.636 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Phil)
Phil
21:48:14.637 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Zhou)
Zhou
21:48:14.637 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Full)
Full
21:48:14.637 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Stack)
Stack
21:48:14.637 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(Programmer)
Programmer
21:48:14.637 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(AWS)
AWS
21:48:14.637 [Test worker] INFO reactor.Flux.ConcatArray.1 -- onNext(DevOps)
DevOps
21:48:14.638 [Test worker] ERROR reactor.Flux.ConcatArray.1 -- onError(java.lang.RuntimeException: Intent to throw an exception within Flux Stream)
21:48:14.638 [Test worker] ERROR reactor.Flux.ConcatArray.1 -- 
java.lang.RuntimeException: Intent to throw an exception within Flux Stream
	at com.geekchow.webflux.MonoFluxTest.FluxTest(MonoFluxTest.java:23)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	...
Intent to throw an exception within Flux Stream
```



### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.6/gradle-plugin/reference/html/#build-image)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web.reactive)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

