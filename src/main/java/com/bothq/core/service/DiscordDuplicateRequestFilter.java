package com.bothq.core.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

public class DiscordDuplicateRequestFilter implements ExchangeFilterFunction {
    private final ConcurrentHashMap<CacheKey, Mono<ClientResponse>> cache = new ConcurrentHashMap<>();

    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest request,
                                       @NotNull ExchangeFunction next) {
        CacheKey key = getKey(request);

        if (key == null) {
            return next.exchange(request);
        }



        return cache.computeIfAbsent(
                key,
                (k) -> {
                    System.out.println("Computing");

                    return next.exchange(request)
                            .map(response -> {
                                System.out.println("Mutating Response");

                                return response
                                        .mutate()
                                        .body(dataBufferFlux -> dataBufferFlux
    //                                                .doOnEach(dataBufferSignal -> {
    //                                                    var dataBuffer = dataBufferSignal.get();
    //                                                    if (dataBuffer == null)
    //                                                        return;
    //
    //                                                    var newDataBuffer = ByteBuffer.allocate(dataBuffer.readableByteCount());
    //                                                    dataBuffer.toByteBuffer(newDataBuffer);
    //
    //
    //                                                    return DefaultDataBufferFactory.sharedInstance.wrap(newDataBuffer);
    //                                                })
                                                        .flatMap(dataBuffer -> {
                                                            System.out.println("Creating ByteBuffer Cache");
                                                            var newDataBuffer = ByteBuffer.allocate(dataBuffer.readableByteCount());
                                                            dataBuffer.toByteBuffer(newDataBuffer);

                                                            return Flux.just(newDataBuffer);
                                                        })
                                                        .cache()
                                                        .flatMap(byteBuffer -> {
                                                            System.out.println("Creating flux from ByteBuffer Cache");
                                                            return Flux.just(DefaultDataBufferFactory
                                                                    .sharedInstance
                                                                    .wrap(byteBuffer));
                                                        })
                                        ).build();
                            })
                            .doOnNext(response -> cache.remove(k))
                            .cache();
                }
        );
    }






    private CacheKey getKey(@NotNull ClientRequest request) {
        if (request.method() != HttpMethod.GET) {
            return null;
        }

        return new CacheKey(
                request
                        .url(),
                request
                        .headers()
                        .getFirst(HttpHeaders.AUTHORIZATION)
        );
    }



    private record CacheKey(java.net.URI Url, String authHeader){}
}
