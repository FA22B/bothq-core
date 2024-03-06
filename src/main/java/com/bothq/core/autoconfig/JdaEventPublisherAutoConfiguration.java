package com.bothq.core.autoconfig;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;

@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)

public class JdaEventPublisherAutoConfiguration {

    public JdaEventPublisherAutoConfiguration(JDA jda, ApplicationEventPublisher publisher) {

        // Add event listener to JDA
        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGenericEvent(@NotNull GenericEvent event) {
                publisher.publishEvent(new PayloadApplicationEvent<>(jda, event));
            }
        });
        
        // Trigger Ready event due to AwaitReady call in JDA instance creation
        publisher.publishEvent(new PayloadApplicationEvent<>(jda, new ReadyEvent(jda)));
    }

}
