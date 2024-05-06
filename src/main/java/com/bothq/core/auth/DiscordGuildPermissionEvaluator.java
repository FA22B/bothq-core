package com.bothq.core.auth;

import net.dv8tion.jda.api.Permission;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * Can be used as PreAuthentication for {@link org.springframework.web.bind.annotation.RestController}s
 * <p>
 * @example {@code @PreAuthorize("@DiscordGuildPermissionEvaluator.hasPermission(#guildId, 'Administrator')")}
 */
@Component("discordPermissionEvaluator")
public class DiscordGuildPermissionEvaluator {

    private final ObjectProvider<UserInfoProvider> userInfoProviders;

    public DiscordGuildPermissionEvaluator(ObjectProvider<UserInfoProvider> userInfoProviders) {
        this.userInfoProviders = userInfoProviders;
    }


    public UserInfoProvider getUserInfoProvider() {
        return userInfoProviders.getObject();
    }

    public boolean hasPermission(String guildId, Permission permission) {
        if (!getUserInfoProvider()
                .getGuilds()
                .containsKey(guildId)){
            // throw new AccessDeniedException("User is not a member of the guild with id '" + guildId + "'");

            return false;
        }

        return getUserInfoProvider()
                .getGuilds()
                    .get(guildId)
                .getPermissions()
                    .contains(permission);
    }


    public boolean hasPermission(String guildId, String permissionName) throws IllegalArgumentException {
        return hasPermission(guildId, Permission.valueOf(permissionName));
    }
}
