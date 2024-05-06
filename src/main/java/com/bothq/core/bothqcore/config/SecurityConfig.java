package com.bothq.core.bothqcore.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.CorsFilter;

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
}
