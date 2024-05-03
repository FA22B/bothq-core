package com.bothq.core.repository;

import com.bothq.core.entity.PluginConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PluginConfigurationRepository extends JpaRepository<PluginConfiguration, Long> {
    // Custom queries can be added here
}
