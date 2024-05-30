package com.bothq.core.repository;

import com.bothq.core.entity.Plugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PluginRepository extends JpaRepository<Plugin, Long> {
    Optional<Plugin> findByPluginId(String pluginId);
    @NotNull
    List<Plugin> findAll();
}
