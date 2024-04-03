package com.bothq.core.bothqcore.dao;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.dv8tion.jda.api.Permission;

import java.io.IOException;
import java.util.EnumSet;

public class DiscordPermissionDeserializer extends JsonDeserializer<EnumSet<Permission>>
{

    @Override
    public EnumSet<Permission> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String string = p.getValueAsString();

        return Permission.getPermissions(Long.parseLong(string));
    }
}
