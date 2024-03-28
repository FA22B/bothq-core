package com.bothq.core.bothqcore.auth;

import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class DiscordGuildTrustResolver extends AuthenticationTrustResolverImpl {
    @Override
    public boolean isAuthenticated(Authentication authentication) {
        if (!super.isAuthenticated(authentication)) return false;

        return true;
    }
}
