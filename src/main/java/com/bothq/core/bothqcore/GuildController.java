package com.bothq.core.bothqcore;


import com.bothq.core.bothqcore.auth.UserInfoProvider;
import com.bothq.core.bothqcore.dao.DiscordGuild;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
//@PreAuthorize("@discordPermissionEvaluator.hasPermission(#guildId, 'Administrator')")
@PreAuthorize("@discordPermissionEvaluator.hasPermission(#guildId, T(net.dv8tion.jda.api.Permission).ADMINISTRATOR)")
@RequestMapping("/api/v1/guild/{guildId}")
public class GuildController {
    private final ObjectProvider<UserInfoProvider> userInfoProviders;

    public GuildController(ObjectProvider<UserInfoProvider> userInfoProviders) {
        this.userInfoProviders = userInfoProviders;
    }

    public UserInfoProvider getUserInfoProvider() {
        return userInfoProviders.getObject();
    }

    @GetMapping(value = "",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public DiscordGuild getGuild(@PathVariable String guildId){
        return getUserInfoProvider()
                .getGuilds()
                .get(guildId);
    }
}

