package com.bothq.core.auth;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
public class DiscordGuildTrustResolver extends AuthenticationTrustResolverImpl {

    final ObjectProvider<UserInfoProvider> userInfoProviders;

    public DiscordGuildTrustResolver(ObjectProvider<UserInfoProvider> userInfoProviders) {
        this.userInfoProviders = userInfoProviders;
    }


    UserInfoProvider getUserInfoProvider(){
        return userInfoProviders.getObject();
    }


    @Override
    public boolean isAuthenticated(Authentication authentication)  {
        if (!super.isAuthenticated(authentication))
            return false;

        return getUserInfoProvider()
                .checkAuthentication(UserInfoProvider.Field.Guild);
    }


    @Bean(name = "GuildAuthManager")
    public static AuthorizationManager<RequestAuthorizationContext> createAuthManager(
            ObjectProvider<UserInfoProvider> userInfoProviders
    ){
        AuthenticatedAuthorizationManager<RequestAuthorizationContext> manager =
                AuthenticatedAuthorizationManager.authenticated();

        manager.setTrustResolver(new DiscordGuildTrustResolver(userInfoProviders));
        return manager;
    }
}
