package com.bothq.core.repository;

import com.bothq.core.entity.ServerPlugin;
import com.bothq.core.entity.ServerPluginId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerPluginRepository extends JpaRepository<ServerPlugin, ServerPluginId> {
}
