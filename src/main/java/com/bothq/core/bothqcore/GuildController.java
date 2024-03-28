package com.bothq.core.bothqcore;


import com.bothq.core.bothqcore.auth.user.AuthenticatedDiscordUser;
import com.bothq.core.bothqcore.dao.DiscordGuild;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;


@RestController()
@PreAuthorize("@discordPermissionEvaluator.hasPermission(#guildId, 'Administrator')")
// @PreAuthorize("@discordPermissionEvaluator.hasPermission(#guildId)")
// @PreAuthorize("@discordPermissionEvaluator.hasPermission()")
@RequestMapping("/api/v1/guild/{guildId}")
public class GuildController {
    @GetMapping(value = "",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public DiscordGuild getGuild(@PathVariable String guildId){
        AuthenticatedDiscordUser user = (AuthenticatedDiscordUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return user.getGuilds().get(guildId);
    }
}

