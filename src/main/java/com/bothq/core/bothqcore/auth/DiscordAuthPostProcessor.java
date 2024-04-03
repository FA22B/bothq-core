package com.bothq.core.bothqcore.auth;

import com.bothq.core.bothqcore.auth.user.AuthenticatedDiscordUser;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;


/**
 * Further configures the LoginAuthenticationFilter.
 */
public class DiscordAuthPostProcessor implements ObjectPostProcessor<OAuth2LoginAuthenticationFilter> {

        /**
         * Changes the Authentication to be of type {@link DiscordAuthentication}
         */
        @Override
        public <O extends OAuth2LoginAuthenticationFilter> O postProcess(O authFilter) {
            authFilter.setAuthenticationResultConverter(this::changeAuthType);

            return authFilter;
        }

        private DiscordAuthentication changeAuthType(OAuth2LoginAuthenticationToken loginToken){
            DefaultOAuth2User oldUser = (DefaultOAuth2User) loginToken.getPrincipal();

            var newUser = new AuthenticatedDiscordUser(
                    oldUser.getAuthorities(),
                    oldUser.getAttributes(),
                    Collections.emptyMap()
            );


            return new DiscordAuthentication(
                    newUser,
                    loginToken.getAuthorities(),
                    loginToken.getClientRegistration().getRegistrationId()
            );
        }
}
