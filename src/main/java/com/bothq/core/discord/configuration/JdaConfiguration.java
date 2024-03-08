package com.bothq.core.discord.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("spring.discord")
public class JdaConfiguration {
    private String token;
    private String proxyHost;
    private int proxyPort;
}
