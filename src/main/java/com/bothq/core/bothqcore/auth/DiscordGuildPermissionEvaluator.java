package com.bothq.core.bothqcore.auth;

import com.bothq.core.bothqcore.auth.user.AuthenticatedDiscordUser;
import net.dv8tion.jda.api.Permission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Can be used as PreAuthentication for {@link org.springframework.web.bind.annotation.RestController}s
 * <p>
 * @example {@code @PreAuthorize("@DiscordGuildPermissionEvaluator.hasPermission(#guildId, 'Administrator')")}
 */
@Component("discordPermissionEvaluator")
public class DiscordGuildPermissionEvaluator {
    public boolean hasPermission(String guildId, String permissionName) {
        AuthenticatedDiscordUser discordUser = (AuthenticatedDiscordUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!discordUser.getGuilds().containsKey(guildId)){
            // throw new AccessDeniedException("User is not a member of the guild with id '" + guildId + "'");

            return false;
        }

        Permission permission = Permission.valueOf(permissionName.toUpperCase());

        System.out.println(permission);

        return discordUser
                .getGuilds()
                    .get(guildId)
                .getPermissions()
                    .contains(permission);
    }
}
