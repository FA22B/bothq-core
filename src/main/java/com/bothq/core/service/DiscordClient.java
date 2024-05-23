package com.bothq.core.service;


import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class DiscordClient {
    private final WebClient webClient;

    /** @noinspection SpringJavaInjectionPointsAutowiringInspection*/
    public DiscordClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        var oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        this.webClient = WebClient
                .builder()
                    .apply(oauth2Client.oauth2Configuration())
                    .baseUrl("https://discord.com/api/v10")
                    .filter(new DiscordDuplicateRequestFilter())
                    .filter(new Discord429Filter())
                .build();
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
//                .onStatus(httpStatusCode -> httpStatusCode == HttpStatus.TOO_MANY_REQUESTS, response -> {
//                    throw new 429
//                })
                    .onStatus(HttpStatusCode::isError, (response) -> {
                        System.out.println(response);
                        throw new AccessDeniedException("Error");
                    })
                .bodyToMono(elementTypeRef);
    }

    public <V> Mono<V> getDiscordEndpoint(@NotNull String endpoint,
                                          @NotNull ParameterizedTypeReference<V> elementTypeRef) {
        return rawGetDiscordEndpoint(endpoint, elementTypeRef);
    }
}

