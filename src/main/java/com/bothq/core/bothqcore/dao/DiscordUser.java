package com.bothq.core.bothqcore.dao;


import lombok.Getter;

@Getter
public class DiscordUser {
    private String name;
    private String id;


    public long getId() {
        return Long.parseLong(id);
    }
}
