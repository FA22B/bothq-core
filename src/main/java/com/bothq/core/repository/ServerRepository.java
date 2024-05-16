package com.bothq.core.repository;

import com.bothq.core.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
}
