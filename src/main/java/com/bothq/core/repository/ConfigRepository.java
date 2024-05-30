package com.bothq.core.repository;

import com.bothq.core.entity.PluginConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<PluginConfig, Long> {
    Optional<PluginConfig> findPluginConfigByServerPlugin_ServerIdAndUniqueId(Long serverId, String uniqueId);
    Optional<PluginConfig> findPluginConfigByServerPlugin_ServerIdAndServerPlugin_PluginIdAndUniqueId(Long serverId, Long pluginId, String uniqueId);


}
