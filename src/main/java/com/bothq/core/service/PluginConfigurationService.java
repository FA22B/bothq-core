package com.bothq.core.service;

import com.bothq.core.entity.Plugin;
import com.bothq.core.entity.PluginConfig;
import com.bothq.core.entity.Server;
import com.bothq.core.plugin.LoadedPlugin;
import com.bothq.core.plugin.config.ConfigGroup;
import com.bothq.core.plugin.config.component.BaseComponent;
import com.bothq.core.repository.ConfigRepository;
import com.bothq.core.repository.PluginRepository;
import com.bothq.core.repository.ServerRepository;
import com.bothq.lib.plugin.config.IConfigurable;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PluginConfigurationService {

    private final ConfigRepository configRepository;
    private final ServerRepository serverRepository;
    private final PluginRepository pluginRepository;

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
            onServerJoin(new GuildJoinEvent(jda, 1337, server));
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

                        var existingConfig = configRepository.findPluginConfigByServerIdAndUniqueId(guild.getIdLong(), toCheck.getUniqueId());
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
                                            .server(server)
                                            .plugin(plugin)
                                            .uniqueId(toCheck.getUniqueId())
                                            .value(defaultValueString)
                                            .isEnabled(true).build();
                            configRepository.save(newConfig);
                        }
                    }
                }
            }
        }
    }

    public <T> T getConfigurationValue(long serverId, String pluginId, String configUniqueId, T defaultValue) {
        var server = serverRepository.findById(serverId).orElseThrow(() -> new RuntimeException("Server not found"));
        var plugin = pluginRepository.findByPluginId(pluginId).orElseThrow(() -> new RuntimeException("Plugin not found"));
        var config = configRepository.findPluginConfigByServerIdAndPluginIdAndUniqueId(server.getId(), plugin.getId(), configUniqueId);

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

    private boolean isWrapperType(Class<?> clazz) {
        return clazz == Boolean.class || clazz == Byte.class || clazz == Character.class ||
                clazz == Double.class || clazz == Float.class || clazz == Integer.class ||
                clazz == Long.class || clazz == Short.class || clazz == String.class;
    }

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
}
