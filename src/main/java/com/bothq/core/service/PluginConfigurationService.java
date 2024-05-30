package com.bothq.core.service;

import com.bothq.core.entity.*;
import com.bothq.core.exceptions.PluginNotFoundException;
import com.bothq.core.exceptions.ServerNotFoundException;
import com.bothq.core.plugin.LoadedPlugin;
import com.bothq.core.plugin.config.ConfigGroup;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import com.bothq.core.repository.ConfigRepository;
import com.bothq.core.repository.PluginRepository;
import com.bothq.core.repository.ServerPluginRepository;
import com.bothq.core.repository.ServerRepository;
import com.bothq.lib.plugin.config.IConfigurable;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.jetbrains.annotations.Contract;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PluginConfigurationService {

    private final ConfigRepository configRepository;
    private final ServerRepository serverRepository;
    private final PluginRepository pluginRepository;
    private final ServerPluginRepository serverPluginRepository;

    private final JDA jda;

    private final ObjectMapper objectMapper;

    @Getter
    private static PluginConfigurationService instance;

    @PostConstruct
    private void init() {
        // Apply singleton instance
        instance = this;

        // Initialize existing servers
        for (var server : jda.getGuilds()) {
            onServerJoin(new GuildJoinEvent(jda, 0, server));
        }
    }

    @EventListener(GuildJoinEvent.class)
    public void onServerJoin(GuildJoinEvent event) {

        // Check if there is already a server entry
        var existingServer = serverRepository.findById(event.getGuild().getIdLong());
        if (existingServer.isEmpty()) {

            // Create new server entity and save it
            var newServer = new Server(event.getGuild().getIdLong());
            serverRepository.save(newServer);
        }
    }

    public void registerPlugin(LoadedPlugin plugin) {

        // Check if there is already a plugin entry
        var pluginEntry = pluginRepository.findByPluginId(plugin.getPluginId());
        if (pluginEntry.isEmpty()) {

            // Create new plugin entity and save it
            var newPlugin = new Plugin(plugin.getPluginId());
            pluginRepository.save(newPlugin);
        }
    }

    @SneakyThrows
    public void createDefaultValues(List<LoadedPlugin> loadedPlugins) {

        // For every server and every plugin, create default values in database
        for (var guild : jda.getGuilds()) {

            // Get the server
            var server = serverRepository.findById(guild.getIdLong()).orElseThrow(() -> new RuntimeException("Server not found"));

            for (var loadedPlugin : loadedPlugins) {

                // Get the plugin
                var plugin = pluginRepository.findByPluginId(loadedPlugin.getPluginId()).orElseThrow(() -> new RuntimeException("Plugin not found"));

                for (var config : loadedPlugin.getConfig().getChildren()) {

                    var itemsToCheck = new ArrayList<IConfigurable>();
                    itemsToCheck.add(config);

                    // Check for config group
                    if (config instanceof ConfigGroup configGroup) {
                        // Remove config group from items to check
                        itemsToCheck.remove(config);

                        // All the config-group's children items to check
                        itemsToCheck.addAll(configGroup.getChildren());
                    }

                    for (var toCheck : itemsToCheck) {

                        var existingConfig = configRepository.findPluginConfigByServerPlugin_ServerIdAndUniqueId(guild.getIdLong(), toCheck.getUniqueId());
                        if (existingConfig.isEmpty()) {

                            Object defaultValue = null;

                            // Check for base component instance
                            if (toCheck instanceof BaseComponent<?, ?> baseComponent) {
                                defaultValue = baseComponent.getDefaultValue();
                            }

                            // Create default value string
                            String defaultValueString = defaultValue == null ? "" : defaultValue.toString();

                            // Check for non-primitive type
                            if (defaultValue != null) {
                                var clazz = defaultValue.getClass();
                                if (!clazz.isPrimitive() && !isWrapperType(clazz) && clazz != String.class) {
                                    defaultValueString = objectMapper.writeValueAsString(defaultValue);
                                }
                            }



                            // Create new config entry
                            var newConfig =
                                    PluginConfig.builder()
                                            // .server(server)
                                            // .plugin(plugin)
                                            .uniqueId(toCheck.getUniqueId())
                                            .value(defaultValueString)
                                            //.isEnabled(true)
                                            .build();

                            configRepository.save(newConfig);
                        }
                    }
                }
            }
        }
    }


    @Contract(pure = true)
    public Map<String, Long> getPluginNameToIdMapping(){
        return pluginRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Plugin::getPluginId,
                        Plugin::getId)
                );
    }

    @Contract(pure = true)
    public String getConfigId(long pluginId) {
        return pluginRepository.findById(pluginId).orElseThrow(PluginNotFoundException::new).getPluginId();
    }

    @Contract(pure = true)
    public <T> T getConfigurationValue(long serverId, String pluginId, String configUniqueId, T defaultValue) {
        var server = serverRepository.findById(serverId).orElseThrow(ServerNotFoundException::new);
        var plugin = pluginRepository.findByPluginId(pluginId).orElseThrow(PluginNotFoundException::new);

        var config = configRepository.findPluginConfigByServerPlugin_ServerIdAndServerPlugin_PluginIdAndUniqueId(server.getId(), plugin.getId(), configUniqueId);

        if (config.isEmpty()) {
            return defaultValue;
        }

        var value = config.get().getValue();
        var clazz = defaultValue.getClass();

        try {
            if (clazz.isPrimitive() || isWrapperType(clazz) || clazz == String.class) {
                return convertToPrimitive(value, clazz);
            } else {
                return objectMapper.readValue(value, objectMapper.constructType(clazz));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert configuration value", e);
        }
    }

    @Contract(pure = true)
    private boolean isWrapperType(Class<?> clazz) {
        return clazz == Boolean.class || clazz == Byte.class || clazz == Character.class ||
                clazz == Double.class || clazz == Float.class || clazz == Integer.class ||
                clazz == Long.class || clazz == Short.class || clazz == String.class;
    }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    private <T> T convertToPrimitive(String value, Class<?> clazz) {
        if (clazz == boolean.class || clazz == Boolean.class) return (T) Boolean.valueOf(value);
        if (clazz == byte.class || clazz == Byte.class) return (T) Byte.valueOf(value);
        if (clazz == char.class || clazz == Character.class) return (T) Character.valueOf(value.charAt(0));
        if (clazz == double.class || clazz == Double.class) return (T) Double.valueOf(value);
        if (clazz == float.class || clazz == Float.class) return (T) Float.valueOf(value);
        if (clazz == int.class || clazz == Integer.class) return (T) Integer.valueOf(value);
        if (clazz == long.class || clazz == Long.class) return (T) Long.valueOf(value);
        if (clazz == short.class || clazz == Short.class) return (T) Short.valueOf(value);
        if (clazz == String.class) return (T) value;
        throw new IllegalArgumentException("Unsupported primitive type: " + clazz);
    }


    public boolean enablePlugin(long serverId, long pluginId, boolean enabled){
        ServerPluginId serverPluginId = new ServerPluginId(serverId, pluginId);
        Optional<ServerPlugin> serverPluginOpt = serverPluginRepository.findById(serverPluginId);

        if (serverPluginOpt.isPresent()) {
            ServerPlugin serverPlugin = serverPluginOpt.get();
            serverPlugin.setIsEnabled(enabled);
            serverPluginRepository.save(serverPlugin);
        }
        else {
            // Check if the server and plugin exist
            Optional<Server> serverOpt = serverRepository.findById(serverId);
            Optional<Plugin> pluginOpt = pluginRepository.findById(pluginId);


            if (pluginOpt.isEmpty()){
                throw new PluginNotFoundException();
            }

            Server server;
            if (serverOpt.isEmpty()){
                server = serverRepository.save(new Server(serverId));
            }
            else {
                server = serverOpt.get();
            }

            ServerPlugin serverPlugin = ServerPlugin.builder()
                    .server(server)
                    .plugin(pluginOpt.get())
                    .isEnabled(enabled)
                    .build();


            serverPluginRepository.save(serverPlugin);
        }

        return enabled;
    }

    public boolean getEnabled(String pluginId,
                              long serverId,
                              boolean defaultValue) {

        var server = serverRepository.findById(serverId).orElseThrow(ServerNotFoundException::new);
        var plugin = pluginRepository.findByPluginId(pluginId).orElseThrow(PluginNotFoundException::new);

        ServerPluginId serverPluginId = new ServerPluginId(server.getId(), plugin.getId());
        Optional<ServerPlugin> serverPluginOpt = serverPluginRepository.findById(serverPluginId);

        if (serverPluginOpt.isEmpty())
            return defaultValue;

        return serverPluginOpt.get().getIsEnabled();
    }
}
