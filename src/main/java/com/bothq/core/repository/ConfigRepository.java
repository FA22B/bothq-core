package com.bothq.core.repository;

import com.bothq.core.entity.PluginConfig;
import com.bothq.core.entity.ServerPlugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<PluginConfig, Long> {
    Optional<PluginConfig> findPluginConfigByServerPlugin_ServerIdAndUniqueId(Long serverId, String uniqueId);
    Optional<PluginConfig> findPluginConfigByServerPlugin_ServerIdAndServerPlugin_PluginIdAndUniqueId(Long serverId, Long pluginId, String uniqueId);

    Optional<PluginConfig> findPluginConfigByServerPluginAndUniqueId(ServerPlugin serverPlugin, String uniqueId);

    @NotNull
    @Override
    <S extends PluginConfig> S save(S entity);
}
