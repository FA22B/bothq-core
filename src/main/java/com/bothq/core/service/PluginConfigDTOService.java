package com.bothq.core.service;

import com.bothq.core.dto.get.ConcretePluginConfigGetDTO;
import com.bothq.core.dto.get.PluginConfigGetDTO;
import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import com.bothq.core.dto.get.group.ConcreteGroupConfigGetDTO;
import com.bothq.core.plugin.LoadedPlugin;
import com.bothq.core.plugin.config.Config;
import com.bothq.core.plugin.config.ConfigGroup;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PluginConfigDTOService {

    private final PluginConfigurationService pluginConfigurationService;
    private final PluginLoaderService pluginLoaderService;

    public ConcretePluginConfigGetDTO getConcretePluginConfiguration(long serverId, long pluginId) {
        // Get the config
        var config = getConfig(pluginId);

        return new ConcretePluginConfigGetDTO(
                200,
                "Success",
                pluginId,
                "group",
                config.getUniqueId(),
                config.isEnabled(serverId),
                config.getDisplayName(),
                config.getDescription(),
                getPluginConfigDTO(serverId, config));
    }

    public PluginConfigGetDTO getPlugin(long pluginId) {
        // Get the config
        var config = getConfig(pluginId);

        return new PluginConfigGetDTO(
                200,
                "Success",
                pluginId,
                "group",
                config.getUniqueId(),
                config.getDisplayName(),
                config.getDescription());
    }


    public List<PluginConfigGetDTO> getAllPlugins(){
        Map<String, Long> pluginNameToIdMapping = pluginConfigurationService.getPluginNameToIdMapping();


        return pluginLoaderService
                .getLoadedPlugins()
                .stream()
                .map(LoadedPlugin::getConfig)
                .map(config -> new PluginConfigGetDTO(
                        200,
                        "Success",
                        pluginNameToIdMapping.get(config.getPluginId()),
                        "group",
                        config.getUniqueId(),
                        config.getDisplayName(),
                        config.getDescription()
                ))
                .collect(Collectors.toList());
    }




    private Config getConfig(long pluginId) {
        var configId = pluginConfigurationService.getConfigId(pluginId);

        // Try to find the plugin
        LoadedPlugin foundPlugin = null;
        for (var plugin : pluginLoaderService.getLoadedPlugins()) {
            if (Objects.equals(plugin.getPluginId(), configId)) {
                foundPlugin = plugin;
                break;
            }
        }

        // Plugin not found
        if (foundPlugin == null) {
            throw new RuntimeException(String.format("Plugin ID was not found by numeral value '%d'", pluginId));
        }

        // Return the config
        return foundPlugin.getConfig();
    }

    private List<IConcreteConfigGetDTO> getPluginConfigDTO(long serverId, ConfigGroup configGroup) {

        // Prepare the return value
        var returnValue = new ArrayList<IConcreteConfigGetDTO>();

        for (var configEntry : configGroup.getChildren()) {

            if (configEntry instanceof ConfigGroup subGroup) {
                var group = new ConcreteGroupConfigGetDTO(
                        "group",
                        subGroup.getUniqueId(),
                        subGroup.getDisplayName(),
                        getPluginConfigDTO(serverId, subGroup));
                returnValue.add(group);
            } else if (configEntry instanceof BaseComponent<?, ?> baseComponent) {
                returnValue.add(baseComponent.getConcreteConfigDTO(serverId));
            }
        }

        return returnValue;
    }


    public void enableServerPlugin(long serverId, long pluginId, boolean enabled){
        pluginConfigurationService.enablePlugin(serverId, pluginId, enabled);
    }
}
