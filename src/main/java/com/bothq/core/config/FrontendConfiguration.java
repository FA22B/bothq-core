package com.bothq.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("frontend")
public class FrontendConfiguration {
    /**
     * URI to redirect to after successful log in.
     */
    private String redirectUri;

    /**
     * URI to redirect to after failed log in.
     */
    private String redirectErrorUri;
}
