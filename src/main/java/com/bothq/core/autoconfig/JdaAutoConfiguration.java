package com.bothq.core.autoconfig;

import com.bothq.core.discord.configuration.JdaConfiguration;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(JdaConfiguration.class)

@RequiredArgsConstructor
public class JdaAutoConfiguration {

    private final JdaConfiguration jdaConfiguration;

    @Bean
    @ConditionalOnMissingBean
    public JDA jda() throws InterruptedException {

        return JDABuilder.createDefault(jdaConfiguration.getToken())
                .setStatus(OnlineStatus.ONLINE)
                .enableCache(CacheFlag.MEMBER_OVERRIDES)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build()
                .awaitReady();

    }
}
