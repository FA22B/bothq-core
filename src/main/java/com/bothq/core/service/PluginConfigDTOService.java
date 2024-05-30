package com.bothq.core.service;

import com.bothq.core.dto.get.ConcretePluginConfigGetDTO;
import com.bothq.core.dto.get.PluginConfigGetDTO;
import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import com.bothq.core.dto.get.group.ConcreteGroupConfigGetDTO;
import com.bothq.core.dto.put.IBaseConfigPutDTO;
import com.bothq.core.dto.put.IGroupConfigPutDTO;
import com.bothq.core.dto.put.PluginConfigPutDTO;
import com.bothq.core.exceptions.ConfigNotFoundException;
import com.bothq.core.plugin.LoadedPlugin;
import com.bothq.core.plugin.config.Config;
import com.bothq.core.plugin.config.ConfigGroup;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import com.bothq.lib.plugin.config.IConfigGroup;
import com.bothq.lib.plugin.config.IConfigurable;
import com.bothq.lib.plugin.config.component.IUnselectedServerComponent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
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



    public void updateConfig(long serverId, long pluginId, PluginConfigPutDTO pluginConfigPutDTO) {
        Config config = getConfig(pluginId);

        if (!matchConfig(serverId, pluginConfigPutDTO, config))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid config format");

        pluginConfigurationService.enablePlugin(serverId, pluginId, pluginConfigPutDTO.getEnabled(), config);


        rawUpdateConfig(serverId, pluginId, pluginConfigPutDTO);
    }


    private boolean matchConfig(long serverId, IGroupConfigPutDTO groupConfigPutDTO, IConfigGroup configGroup){
        Map<String, IBaseConfigPutDTO<?, ?>> allValues = Arrays
                .stream(groupConfigPutDTO.getValue())
                .collect(Collectors.toMap(IBaseConfigPutDTO::getUniqueId, child -> child));

        for (IConfigurable child : configGroup.getChildren()) {
            if (child instanceof IConfigGroup childConfigGroup){
                var foundPutConfig = allValues.remove(childConfigGroup.getUniqueId());

                if (foundPutConfig == null)
                    continue;

                if (!(foundPutConfig instanceof IGroupConfigPutDTO childGroupConfigPutDTO))
                    throw new ConfigNotFoundException();

                if (!matchConfig(serverId, childGroupConfigPutDTO, childConfigGroup))
                    return false;
            }
            if (child instanceof IUnselectedServerComponent<?> component){
                var selected = component.get(serverId);
                var foundPutConfig = allValues.remove(component.getUniqueId());

                if (foundPutConfig == null)
                    continue;

                if (!selected.isAssignable(foundPutConfig.getValue()))
                    return false;
            }
        }

        if (!allValues.isEmpty())
            throw new ConfigNotFoundException();

        return true;
    }

    @Transactional
    private void rawUpdateConfig(long serverId, long pluginId, IGroupConfigPutDTO groupConfigPutDTO){
        for (IBaseConfigPutDTO<?, ?> childPutDTO : groupConfigPutDTO.getValue()) {
            if (childPutDTO instanceof IGroupConfigPutDTO childGroupConfigPutDTO){
                rawUpdateConfig(serverId, pluginId, childGroupConfigPutDTO);
                return;
            }


            String uniqueId = childPutDTO.getUniqueId();
            Object value = childPutDTO.getValue();

            pluginConfigurationService.setConfigValue(serverId, pluginId, uniqueId, value);
        }
    }

    public void deleteServerPlugin(long serverId, long pluginId) {
        pluginConfigurationService.deletePluginServer(serverId, pluginId);
    }
}
