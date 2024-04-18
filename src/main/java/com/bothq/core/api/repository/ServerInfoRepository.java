package com.bothq.core.api.repository;

import com.bothq.core.api.model.ServerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerInfoRepository extends JpaRepository<ServerInfo, String> {
}
