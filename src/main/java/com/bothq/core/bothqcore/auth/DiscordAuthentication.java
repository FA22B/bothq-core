package com.bothq.core.bothqcore.auth;

import com.bothq.core.bothqcore.auth.user.AuthenticatedDiscordUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.security.auth.Subject;
import java.util.Collection;

public class DiscordAuthentication extends OAuth2AuthenticationToken {
    /**
     * Constructs an {@code OAuth2AuthenticationToken} using the provided parameters.
     *
     * @param principal                      the user {@code Principal} registered with the OAuth 2.0 Provider
     * @param authorities                    the authorities granted to the user
     * @param authorizedClientRegistrationId the registration identifier
     */
    public DiscordAuthentication(AuthenticatedDiscordUser principal,
                                 Collection<? extends GrantedAuthority> authorities,
                                 String authorizedClientRegistrationId) {
        super(principal, authorities, authorizedClientRegistrationId);
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }

    @Override
    public AuthenticatedDiscordUser getPrincipal() {
        return (AuthenticatedDiscordUser) super.getPrincipal();
    }
}
