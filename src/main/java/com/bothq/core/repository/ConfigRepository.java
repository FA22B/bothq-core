package com.bothq.core.repository;

import com.bothq.core.entity.PluginConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<PluginConfig, Long> {
    Optional<PluginConfig> findPluginConfigByUniqueId(String uniqueId);

    Optional<PluginConfig> findPluginConfigByServerIdAndUniqueId(Long serverId, String uniqueId);

    Optional<PluginConfig> findPluginConfigByServerIdAndPluginIdAndUniqueId(Long serverId, Long pluginId, String uniqueId);
}
