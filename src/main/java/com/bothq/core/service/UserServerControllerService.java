package com.bothq.core.service;

import com.bothq.core.dto.GeneralConfigDTO;
import com.bothq.core.dto.GroupConfigDTO;
import com.bothq.core.dto.PluginConfigDTO;
import com.bothq.core.plugin.LoadedPlugin;
import com.bothq.core.plugin.config.ConfigGroup;
import com.bothq.core.plugin.config.component.BaseComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServerControllerService {

    private final PluginConfigurationService pluginConfigurationService;
    private final PluginLoaderService pluginLoaderService;

    public PluginConfigDTO getPluginConfiguration(long serverId, long pluginId) {

        // Get the string config ID from the numeral database ID
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

        // Get the config
        var config = foundPlugin.getConfig();

        return new PluginConfigDTO(
                200,
                "Success",
                pluginId,
                "group",
                config.getUniqueId(),
                config.isEnabled(),
                config.getDisplayName(),
                getPluginConfigDTO(serverId, config));
    }

    private List<GeneralConfigDTO> getPluginConfigDTO(long serverId, ConfigGroup configGroup) {

        // Prepare the return value
        var returnValue = new ArrayList<GeneralConfigDTO>();

        for (var configEntry : configGroup.getChildren()) {

            if (configEntry instanceof ConfigGroup subGroup) {
                var group = new GroupConfigDTO(
                        "group",
                        subGroup.getUniqueId(),
                        subGroup.isEnabled(),
                        subGroup.getDisplayName(),
                        getPluginConfigDTO(serverId, subGroup));
                returnValue.add(group);
            } else if (configEntry instanceof BaseComponent<?, ?> baseComponent) {
                returnValue.add(baseComponent.getGeneralConfigDTO(serverId));
            }
        }

        return returnValue;
    }
}
