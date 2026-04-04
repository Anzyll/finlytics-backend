package com.anzil.finlytics.audit.repository;

import com.anzil.finlytics.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}