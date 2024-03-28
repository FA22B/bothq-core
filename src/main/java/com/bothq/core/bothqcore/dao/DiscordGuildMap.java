package com.bothq.core.bothqcore.dao;

import com.bothq.core.bothqcore.DiscordGuildListDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;


@JsonDeserialize(using = DiscordGuildListDeserializer.class)
public class DiscordGuildMap extends HashMap<String, DiscordGuild>{

}
