package com.bothq.core.bothqcore.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //.securityMatcher("/api/**")
                .authorizeHttpRequests(matcher -> matcher
                        // .requestMatchers("/oauth2/**").permitAll()
                        // .requestMatchers("/login/**").permitAll()
                        // .anyRequest().permitAll()

                        // TODO use .access(CustomAuthorizationManager)
                        .requestMatchers("/api/**")
                            //.access(getDiscordGuildAccessManager())
                            .authenticated()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().denyAll()
                )
                //.formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(login -> login
                        //.failureHandler((httpServletRequest, httpServletResponse, authenticationException) -> {})
                        // .successHandler()
                        .failureUrl("http://localhost:4200/redirect")
                        .defaultSuccessUrl("http://localhost:4200/redirect?success=true", true)
                        .loginPage("/error")
                        .withObjectPostProcessor(new DiscordAuthPostProcessor())
                )
                .oauth2Client(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
                // .addFilterAfter(
                //         new DiscordGuildFilter(),
                //         AuthorizationFilter.class
                // )
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


//    private AuthorizationManager<RequestAuthorizationContext> getDiscordGuildAccessManager(){
//        AuthenticatedAuthorizationManager<RequestAuthorizationContext> manager =
//                AuthenticatedAuthorizationManager.authenticated();
//
//        manager.setTrustResolver(new DiscordGuildTrustResolver());
//        return manager;
//    }
}
