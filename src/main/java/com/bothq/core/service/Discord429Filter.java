package com.bothq.core.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class Discord429Filter implements ExchangeFilterFunction {

    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest request,
                                       @NotNull ExchangeFunction next) {
        return next.exchange(request)
                .flatMap(clientResponse -> Mono.just(clientResponse)
                        .filter(response -> clientResponse.statusCode().isError())      // if error
                        .flatMap(response -> clientResponse.createException())          // turn current mono into Mono<Exception>
                        .flatMap(Mono::error)                                           // and throw error
                        .thenReturn(clientResponse)                                     // return client response, on error return current Mono (which is Mono<Exception>)
                )
                .retryWhen(retryWhenTooManyRequests());
    }



    private Retry retryWhenTooManyRequests(){
        return Retry.indefinitely()
                .maxAttempts(1)
                .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests)
                .filter(throwable -> {
                    var temp = (WebClientResponseException.TooManyRequests) throwable;

                    var rateLimitResetHeader = temp.getHeaders().get("X-RateLimit-Reset-After");
                    if (rateLimitResetHeader == null || rateLimitResetHeader.isEmpty()) {
                        return false;
                    }

                    var millisecondDelay = (long)(Double.parseDouble(rateLimitResetHeader.get(0)) * 1000) + 1;

                    return millisecondDelay <= 1000;
                })
                .doBeforeRetryAsync(throwable -> {
                    var temp = (WebClientResponseException.TooManyRequests) throwable.failure();

                    var rateLimitResetHeader = temp.getHeaders().get("X-RateLimit-Reset-After");
                    assert rateLimitResetHeader != null;
                    var millisecondDelay = (long)(Double.parseDouble(rateLimitResetHeader.get(0)) * 1000) + 1;


                    return Mono
                            .delay(Duration.ofMillis(millisecondDelay))
                            .then();
                });
    }


}
