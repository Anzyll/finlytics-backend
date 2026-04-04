package com.anzil.finlytics.audit.service;

import com.anzil.finlytics.audit.entity.AuditLog;
import com.anzil.finlytics.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository repo;

    public void log(Long userId, String action, String entity, Long entityId) {

        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setEntityType(entity);
        log.setEntityId(entityId);

        repo.save(log);
    }
}