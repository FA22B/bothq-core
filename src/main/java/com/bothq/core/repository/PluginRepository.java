package com.bothq.core.repository;

import com.bothq.core.entity.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PluginRepository extends JpaRepository<Plugin, Long> {
    Optional<Plugin> findByPluginId(String pluginId);
}
