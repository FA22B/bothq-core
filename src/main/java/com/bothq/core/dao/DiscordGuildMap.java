package com.bothq.core.dao;

import com.bothq.core.dao.serializer.DiscordGuildListDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;


@JsonDeserialize(using = DiscordGuildListDeserializer.class)
public class DiscordGuildMap extends HashMap<String, DiscordGuild>{

}
