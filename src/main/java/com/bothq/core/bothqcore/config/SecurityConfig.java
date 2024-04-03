package com.bothq.core.bothqcore.config;

import com.bothq.core.bothqcore.auth.DiscordGuildTrustResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Qualifier("GuildAuthManager")
                                                   AuthorizationManager<RequestAuthorizationContext> guildAuthManager) throws Exception {
        return http
                .anonymous(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(matcher -> matcher
                        // Guild Trust resolver
                        .requestMatchers("/api/*/guild/**")
                            .access(guildAuthManager)

                        // Default API Trust Resolver
                        .requestMatchers("/api/**")
                            .access(guildAuthManager)
                        .requestMatchers("/error").permitAll()
                            .anyRequest().denyAll()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                                response.sendError(403, "Not authenticated");
                            }
                        })
                )
                .oauth2Login(login -> login
                        //.failureHandler((httpServletRequest, httpServletResponse, authenticationException) -> {})
                        // .successHandler()
                        .failureUrl("http://localhost:4200/redirect")
                        .defaultSuccessUrl("http://localhost:4200/redirect?success=true", true)
                        // .loginProcessingUrl("/oauth2/")
                )
                .oauth2Client(Customizer.withDefaults())
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("http://localhost:4200/redirect?sucess=false"))
                .build();
    }



    @Bean
    CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Don't do this in production, use a proper list  of allowed origins
        config.setAllowedOrigins(List.of("http://localhost:4200", "https://discord.com"));
        config.setAllowedMethods(List.of("*"));

        // OAuth2LoginAuthenticationFilter

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(64000);
        return loggingFilter;
    }


    // Bug with Intellij Idea, resolved in 2024.1 EAP5 (see https://youtrack.jetbrains.com/issue/IDEA-340546/Default-OAuth2AuthorizedClientManager-bean-not-recognized-for-auto-wiring-when-using-Spring-Security-OAuth2-Client-starter#focus=Comments-27-9215555.0-0)
    /** @noinspection SpringJavaInjectionPointsAutowiringInspection*/
    @Bean
    WebClient.Builder webClientBuilder(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient
                .builder()
                    .apply(oauth2Client.oauth2Configuration());
    }
}
