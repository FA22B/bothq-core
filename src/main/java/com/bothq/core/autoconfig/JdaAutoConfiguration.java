package com.bothq.core.autoconfig;

import com.bothq.core.discord.configuration.JdaConfiguration;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.net.Proxy;

@AutoConfiguration
@EnableConfigurationProperties(JdaConfiguration.class)

@RequiredArgsConstructor
public class JdaAutoConfiguration {

    private final JdaConfiguration jdaConfiguration;

    @Bean
    @ConditionalOnMissingBean
    public JDA jda() throws InterruptedException {

        // Create the base builder
        var builder = JDABuilder.createDefault(jdaConfiguration.getBotToken())
                .setStatus(OnlineStatus.ONLINE)
                .enableCache(CacheFlag.MEMBER_OVERRIDES)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT);

        // Check if a proxy was set
        if (jdaConfiguration.getProxyHost() != null && !jdaConfiguration.getProxyHost().isBlank()) {
            // Apply proxy settings
            builder.setHttpClientBuilder(
                    new OkHttpClient.Builder().proxy(
                            new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                                    jdaConfiguration.getProxyHost(),
                                    jdaConfiguration.getProxyPort()))));
        }

        // Build, await ready and return
        return builder.build().awaitReady();
    }
}
