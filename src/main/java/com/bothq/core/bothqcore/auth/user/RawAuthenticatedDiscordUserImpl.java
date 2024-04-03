package com.bothq.core.bothqcore.auth.user;

import com.bothq.core.bothqcore.dao.DiscordGuild;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Assert;

import java.util.*;

public class RawAuthenticatedDiscordUserImpl implements RawAuthenticatedDiscordUser {
    @Getter
    private Set<GrantedAuthority> authorities;
    @Getter
    private Map<String, Object> attributes;
    @Getter
    private Map<String, DiscordGuild> guilds;

    @Getter
    private final String name;

    public static final String nameAttributeKey = "id";


    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities the authorities granted to the user
     * @param attributes  the attributes about the user
     *                    {@link #getAttributes()}
     * @param guilds      the guilds of the user
     */
    public RawAuthenticatedDiscordUserImpl(Collection<? extends GrantedAuthority> authorities,
                                           Map<String, Object> attributes,
                                           Map<String, DiscordGuild> guilds) {
        Assert.notEmpty(attributes, "attributes cannot be empty");
        if (!attributes.containsKey(nameAttributeKey)) {
            throw new IllegalArgumentException("Missing attribute '" + nameAttributeKey + "' in attributes");
        }


        this.authorities = (authorities != null)
                ? Collections.unmodifiableSet(new LinkedHashSet<>(this.sortAuthorities(authorities)))
                : Collections.unmodifiableSet(new LinkedHashSet<>(AuthorityUtils.NO_AUTHORITIES));

        this.guilds = (guilds != null)
                ? Collections.unmodifiableMap(guilds)
                : Collections.emptyMap();

        
        this.attributes = Collections.unmodifiableMap(attributes);
        this.name = attributes.get(nameAttributeKey).toString();
    }

    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }

    public void setAttributes(Map<String, Object> attributes) {
        Assert.notEmpty(attributes, "attributes cannot be empty");
        if (!attributes.containsKey(nameAttributeKey)) {
            throw new IllegalArgumentException("Missing attribute 'id' in attributes");
        }

        if (attributes.get(nameAttributeKey) != getName()){
            throw new IllegalArgumentException("Attribute 'id' of attributes different from existing id");
        }

        this.attributes = Collections.unmodifiableMap(attributes);
    }

    public void setGuilds(Map<String, DiscordGuild> guilds) {
        this.guilds = (guilds != null)
                ? Collections.unmodifiableMap(guilds)
                : Collections.emptyMap();
    }
}
