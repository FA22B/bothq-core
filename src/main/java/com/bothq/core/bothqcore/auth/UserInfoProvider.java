package com.bothq.core.bothqcore.auth;

import com.bothq.core.bothqcore.dao.DiscordGuild;
import com.bothq.core.bothqcore.dao.DiscordGuildMap;
import com.bothq.core.bothqcore.dao.DiscordUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Component
@RequestScope
public class UserInfoProvider {
    private Map<String, DiscordGuild> guilds;
    private DiscordUser user;


    private boolean attemptedAuthentication = false;
    private boolean authenticated = false;


    private final WebClient webClient;

    public UserInfoProvider(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .clone()
                .baseUrl("https://discord.com/api/v10")
                .build();
    }


    private <T> Mono<T> getDiscordEndpoint(String endpoint,
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
                .bodyToMono(elementTypeRef)
                    .doOnEach((obj) -> this.attemptedAuthentication = true)
                    .doOnSuccess((obj) -> this.authenticated = true);
    }


    public DiscordUser getUser() {
        if (user != null) return user;

        getDiscordEndpoint(
                "/users/@me",
                new ParameterizedTypeReference<DiscordUser>() {
            })
                .doOnSuccess(user -> this.user = user)
                .doOnError(AccessDeniedException.class, e -> {
                    throw e;
                })
                .block();

        return user;

    }

    public Map<String, DiscordGuild> getGuilds() {
        if (guilds != null) return guilds;

        try {

            getDiscordEndpoint(
                    "/users/@me/guilds",
                    new ParameterizedTypeReference<DiscordGuildMap>() {
                    }
            )
                    .doOnSuccess(discordGuildMap -> {
                        guilds = Collections.unmodifiableMap(discordGuildMap);
                    })
                    .doOnError(AccessDeniedException.class, e -> {
                        throw e;
                    })
                    .block();

        } catch (Exception e) {
            ;
        }

        return guilds;

    }


    public boolean checkAuthentication(Field field){
        if (attemptedAuthentication) return authenticated;

        if (field == Field.Guild){
            getGuilds();
        }
        else if (field == Field.User) {
            getUser();
        }

        return authenticated;
    }

    public enum Field {
        User,
        Guild
    }
}
