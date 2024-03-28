package com.bothq.core.bothqcore.auth;

import com.bothq.core.bothqcore.auth.user.AuthenticatedDiscordUser;
import com.bothq.core.bothqcore.dao.DiscordGuildMap;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * This class checks whether the user is Authenticated with every request. This <strong>has</strong> to run every time a user makes a
 * request, in order to ensure that the user still has access rights.
 * <p>
 * This class sets the current users Authentication to a {@link DiscordAuthentication}.
 */
@Component
public class DiscordGuildFilter extends OncePerRequestFilter {
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    private final RestClient restClient;

    public DiscordGuildFilter() {
        restClient = RestClient.create();
    }


    @Override
    public void doFilterInternal(@NotNull HttpServletRequest httpRequest,
                                 @NotNull HttpServletResponse httpResponse,
                                 @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        if (authorizedClientService == null)
        {
            throw new AccessDeniedException("Unauthorized");
        }


        if (!(httpRequest.getUserPrincipal() instanceof OAuth2AuthenticationToken authentication))
        {
            throw new AccessDeniedException("Unauthorized");
        }

        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName()
                );



        if (oAuth2AuthorizedClient == null)
        {
            throw new AccessDeniedException("Unauthorized");
        }

        OAuth2AccessToken token = oAuth2AuthorizedClient.getAccessToken();


        DiscordGuildMap guilds = restClient.get()
                .uri("https://discord.com/api/v10/users/@me/guilds")
                .header("Authorization", "Bearer " + token.getTokenValue())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                    .onStatus(httpStatusCode -> httpStatusCode == HttpStatus.UNAUTHORIZED, (request, response) -> {
                        throw new AccessDeniedException("Unauthorized");
                    })
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw new AccessDeniedException("Error");
                    })
                .body(new ParameterizedTypeReference<>() {});


        var context = SecurityContextHolder.getContext();




        AuthenticatedDiscordUser oldUser = (AuthenticatedDiscordUser) authentication.getPrincipal();

        var newUser = new AuthenticatedDiscordUser(
                oldUser.getAuthorities(),
                oldUser.getAttributes(),
                guilds
        );


        var newAuthorization = new DiscordAuthentication(
                newUser,
                authentication.getAuthorities(),
                authentication.getAuthorizedClientRegistrationId()
        );


        context.setAuthentication(newAuthorization);


        filterChain.doFilter(httpRequest, httpResponse);
    }
}
