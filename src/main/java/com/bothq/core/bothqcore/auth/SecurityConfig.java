package com.bothq.core.bothqcore.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(matcher -> matcher

                        .requestMatchers("/api/**")
                        .authenticated()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().denyAll()
                )
                .oauth2Login(login -> login
                        .failureUrl("http://localhost:4200/redirect")
                        .defaultSuccessUrl("http://localhost:4200/redirect?success=true", true)
                        .loginPage("/error") // basically disables login page
                        .withObjectPostProcessor(new DiscordAuthPostProcessor())
                )
                .oauth2Client(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
                .build();
    }

}
