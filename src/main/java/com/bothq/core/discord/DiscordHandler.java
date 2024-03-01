package com.bothq.core.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DiscordHandler {

    @EventListener(MessageReceivedEvent.class)
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        // TODO: Remove debug
        var channel = event.getChannel();
        if (channel.getIdLong() != 1213088396277321768L) {
            return;
        }

        // Echo back
        if (!event.getAuthor().isBot() && !event.getAuthor().isSystem()) {
            channel.sendMessage("Echo: " + event.getMessage().getContentRaw()).queue();
        }
    }
}
