package com.bothq.core.bothqcore.auth.user;

import com.bothq.core.bothqcore.dao.DiscordGuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Map;
import java.util.function.Supplier;

@RequestScope
@Component
public class UserInfoProvider {
    private Map<String, DiscordGuild> guilds;
    private DiscordUser user;

    private RestClient restClient;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    private RestClient getRestClient() {
        return (restClient == null)
                ? (restClient = RestClient.create())
                : restClient;
    }


    private OAuth2AuthenticationToken getAuthentication() throws Exception {
        if (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal() instanceof OAuth2AuthenticationToken token) {
            return token;
        }

        throw new AccessDeniedException("Unauthorized");
    }


    private String getAccessToken() throws Exception {
        OAuth2AuthenticationToken authentication = getAuthentication();

        if (authorizedClientService == null ||
                authentication == null)
            throw new Exception();

        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName()
                );

        if (oAuth2AuthorizedClient == null) {
            throw new Exception();
        }

        // TODO token refresh service
        return oAuth2AuthorizedClient
                .getAccessToken()
                .getTokenValue();
    }

    private RestClient.RequestHeadersSpec<?> getRestRequest() throws Exception {
        return getRestClient()
                        .get()
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + getAccessToken());
    }


    public DiscordUser getUser() {
        if (user != null) return user;
        return new DiscordUser();
    }
}
