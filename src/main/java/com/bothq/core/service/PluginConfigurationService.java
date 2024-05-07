package com.bothq.core.service;

import com.bothq.core.entity.PluginConfig;
import com.bothq.core.repository.ConfigRepository;
import com.bothq.core.repository.PluginRepository;
import com.bothq.core.repository.ServerRepository;
import com.bothq.core.serialization.ComponentSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PluginConfigurationService {

    private final ConfigRepository configRepository;
    private final ServerRepository serverRepository;
    private final PluginRepository pluginRepository;

    @Transactional
    public void updateConfigurations(Long serverId, String pluginId, List<PluginConfig> updatedConfigs) {
        var server = serverRepository.findById(serverId).orElseThrow(() -> new RuntimeException("Server not found"));
        var plugin = pluginRepository.findByPluginId(pluginId).orElseThrow(() -> new RuntimeException("Plugin not found"));

        updatedConfigs.forEach(config -> {
            var existingConfigOpt = configRepository.findById(config.getConfigId());
            PluginConfig configEntity;
            if (existingConfigOpt.isPresent()) {
                configEntity = existingConfigOpt.get();
            } else {
                // Create a new configuration entry
                configEntity = new PluginConfig();
                configEntity.setServer(server);
                configEntity.setUniqueId(config.getUniqueId()); // Ensure uniqueId is set for new entries
                configEntity.setType("defaultType"); // You need to define how to set the type or adjust it based on actual requirements
            }

            try {
                configEntity.setValue(ComponentSerializer.serialize(config.getValue()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error during serialization", e);
            }

            configEntity.setDisplayName(config.getDisplayName());
            configEntity.setIsEnabled(config.getIsEnabled());
            configRepository.save(configEntity);
        });
    }
}
