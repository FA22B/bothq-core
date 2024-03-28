package com.bothq.core;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("frontend")
public class FrontendConfiguration {
    private String redirectUri;
    private String redirectErrorUri;
}
