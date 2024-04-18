package com.bothq.core.bothqcore.dao;

import com.bothq.core.bothqcore.DiscordPermissionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiscordGuild {
    private String id;
    private String name;
    private String icon;
    private boolean owner;

    @JsonDeserialize(using = DiscordPermissionDeserializer.class)
    private EnumSet<Permission> permissions;
    private String[] features;
}
