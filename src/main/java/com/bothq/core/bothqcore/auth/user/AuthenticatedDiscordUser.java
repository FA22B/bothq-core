package com.bothq.core.bothqcore.auth.user;

import com.bothq.core.bothqcore.dao.DiscordGuild;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;




public class AuthenticatedDiscordUser extends RawAuthenticatedDiscordUserImpl {

    private final RawAuthenticatedDiscordUserImpl rawAuthenticatedDiscordUser;


    /**
     * Constructs an {@code AuthenticatedDiscordUser} using the provided parameters.
     *
     * @param authorities       the authorities granted to the user
     * @param attributes        the attributes about the user
     * @param guilds            the guilds of the user
     */
    public AuthenticatedDiscordUser(
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes,
            Map<String, DiscordGuild> guilds) {
        super(authorities, attributes, guilds);
        rawAuthenticatedDiscordUser = new RawAuthenticatedDiscordUserImpl(
                authorities,
                attributes,
                guilds
        );
    }

    @Override
    public <A> A getAttribute(String name) {
        return super.getAttribute(name);
    }

    /**
     * Returns information about the logged-in user. This information might not be recent.
     *
     * @return The cached Discord User
     */
    public RawAuthenticatedDiscordUser getCachedDiscordUser() {
        return rawAuthenticatedDiscordUser;
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        rawAuthenticatedDiscordUser.setAttributes(attributes);
        super.setAttributes(attributes);
    }

    @Override
    public void setGuilds(Map<String, DiscordGuild> guilds) {
        rawAuthenticatedDiscordUser.setGuilds(guilds);
        super.setGuilds(guilds);
    }

    public void clearNonPersistentData(){
        super.setAttributes(Map.of(nameAttributeKey, getName()));
        super.setGuilds(null);
    }
}
