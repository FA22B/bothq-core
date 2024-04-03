package com.bothq.core.bothqcore.auth.user;

import com.bothq.core.bothqcore.dao.DiscordGuild;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public interface RawAuthenticatedDiscordUser extends OAuth2User {
    /**
     * This does <strong>not</strong> return the name, as the name in discord can change.
     * This instead returns the user id.
     *
     * @return Discord User Id
     */
    @Override
    String getName();

    @Override
    Collection<? extends GrantedAuthority> getAuthorities();

    /**
     * @return User data as collected from discord.
     */
    @Override
    Map<String, Object> getAttributes();

    Map<String, DiscordGuild> getGuilds();
}
