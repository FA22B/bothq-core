package com.bothq.core.bothqcore.dao;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.dv8tion.jda.api.Permission;

import java.io.IOException;
import java.util.EnumSet;

public class DiscordPermissionSerializer extends JsonSerializer<EnumSet<Permission>> {
    @Override
    public void serialize(EnumSet<Permission> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(Permission.getRaw(value));
    }
}
