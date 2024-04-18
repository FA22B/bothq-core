package com.bothq.core.api.repository;

import com.bothq.core.api.model.PluginConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PluginConfigRepository extends JpaRepository<PluginConfig, String> {
}
