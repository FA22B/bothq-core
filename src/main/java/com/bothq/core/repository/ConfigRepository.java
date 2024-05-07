package com.bothq.core.repository;

import com.bothq.core.entity.PluginConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<PluginConfig, Long> {
}
