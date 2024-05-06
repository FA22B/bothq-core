package com.bothq.core.bothqcore.dao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

@Data
public class DiscordGuild {
    private String id;
    private String name;
    private String icon;
    private boolean owner;

    @JsonDeserialize(using = DiscordPermissionDeserializer.class)
    @JsonSerialize(using = DiscordPermissionSerializer.class)
    private EnumSet<Permission> permissions;
    private String[] features;
}
