package com.bothq.core.bothqcore;

import com.bothq.core.bothqcore.dao.DiscordGuild;
import com.bothq.core.bothqcore.dao.DiscordGuildMap;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiscordGuildListDeserializer extends JsonDeserializer<DiscordGuildMap> {
    @Override
    public DiscordGuildMap deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode rootNode = p.getCodec().readTree(p);
        DiscordGuildMap map = new DiscordGuildMap();

        for (JsonNode node: rootNode) {
            DiscordGuild guild = ctx.readTreeAsValue(node, DiscordGuild.class);

            map.put(guild.getId(), guild);
        }

        return map;
    }
}
