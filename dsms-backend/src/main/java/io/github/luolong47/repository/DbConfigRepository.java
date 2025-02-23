package io.github.luolong47.repository;

import io.github.luolong47.entity.DbConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DbConfigRepository extends JpaRepository<DbConfig, String>, JpaSpecificationExecutor<DbConfig> {
} 