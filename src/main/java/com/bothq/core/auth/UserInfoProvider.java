package com.bothq.core.auth;

import com.bothq.core.service.DiscordClient;
import com.bothq.core.dao.DiscordGuild;
import com.bothq.core.dao.DiscordGuildMap;
import com.bothq.core.dao.DiscordUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Component
@RequestScope
public class UserInfoProvider {
    private final DiscordClient discordClient;

    private boolean attemptedAuthentication = false;
    private boolean authenticated = false;




    private Map<String, DiscordGuild> guilds;
    private DiscordUser user;



    public UserInfoProvider(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }


    private <T> Mono<T> getDiscordEndpoint(String endpoint,
                                           ParameterizedTypeReference<T> elementTypeRef) {
        return discordClient
                    .getDiscordEndpoint(endpoint, elementTypeRef)
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

        getDiscordEndpoint(
                "/users/@me/guilds",
                new ParameterizedTypeReference<DiscordGuildMap>() {}
        )
                .doOnSuccess(discordGuildMap ->
                        guilds = Collections.unmodifiableMap(discordGuildMap))
                .doOnError(AccessDeniedException.class, e -> {
                    throw e;
                })
                .block();

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
