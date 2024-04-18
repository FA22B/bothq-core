package com.bothq.core.api.repository;

import com.bothq.core.api.model.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, String> {
}
