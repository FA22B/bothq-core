package com.bothq.core.service;


import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Nonnull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class DiscordClient {
    private final WebClient webClient;
    private final HashMap<String, TypeReferenceAsyncCacheTuple<?>> caches = new HashMap<>();

    public DiscordClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        var oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        this.webClient = WebClient
                .builder()
                    .apply(oauth2Client.oauth2Configuration())
                    .baseUrl("https://discord.com/api/v10")
                .build();
    }


    private String getDiscordUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    private <T> Mono<T> rawGetDiscordEndpoint(String endpoint,
                                              ParameterizedTypeReference<T> elementTypeRef) {

        return webClient
                .get()
                    .uri(endpoint)
                    .attributes(clientRegistrationId("discord-client"))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(httpStatusCode -> httpStatusCode == HttpStatus.UNAUTHORIZED, (response) -> {
                        throw new AccessDeniedException("Unauthorized");
                    })
                    .onStatus(HttpStatusCode::isError, (response) -> {
                        throw new AccessDeniedException("Error");
                    })
                .bodyToMono(elementTypeRef);
    }

    private <V> Mono<V> getFromCache(String endpoint) {
        @SuppressWarnings("unchecked")
        TypeReferenceAsyncCacheTuple<V> cacheTuple = (TypeReferenceAsyncCacheTuple<V>) caches.get(endpoint);    // Because getFromCache can theoretically be called with different elementTypeRefs, this isn't safe.
                                                                                                                // In practice, if getFromCache was called with the wrong TypeParameter something already went wrong.

        return Mono.fromFuture(
                cacheTuple.cache.get(
                        getDiscordUserId(),
                        (k, e) -> rawGetDiscordEndpoint(
                                endpoint,
                                cacheTuple.typeReference
                        )
                                // .subscribeOn(Schedulers.fromExecutor(e)) // Might mess with SpringBoot's own Scheduler?
                                .toFuture()
                )
        );
    }


    private <V> void createCache(@Nonnull String endpoint,
                                 @Nonnull ParameterizedTypeReference<V> elementTypeRef) {
        caches.put(
                endpoint,
                new TypeReferenceAsyncCacheTuple<>(
                        elementTypeRef,
                        Caffeine.newBuilder()
                                .expireAfterWrite(Duration.ofSeconds(1))    // 0 seconds is not reliable on discords side ->
                                                                            // sometimes returns 429 even if the previous request has finished (maybe due to network latency?)
                                .buildAsync()
                )
        );
    }


    public <V> Mono<V> getDiscordEndpoint(@Nonnull String endpoint,
                                          @Nonnull ParameterizedTypeReference<V> elementTypeRef) {


        if (!caches.containsKey(endpoint)) {
            createCache(endpoint, elementTypeRef);
        }

        TypeReferenceAsyncCacheTuple<?> cacheTuple = caches.get(endpoint);
        if (!cacheTuple.typeReference.equals(elementTypeRef)) {
            throw new IllegalArgumentException("Cannot retrieve an object of type '" + elementTypeRef + "', expected type '" + cacheTuple.typeReference + "'");
        }


        return getFromCache(endpoint);
    }

    private record TypeReferenceAsyncCacheTuple<V>(ParameterizedTypeReference<V> typeReference,
                                                   AsyncCache<String, V> cache) {
    }
}

