package com.anzil.finlytics.record.repository;

import com.anzil.finlytics.record.entity.FinancialRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
    List<FinancialRecord> findByUserId(Long userId);
    @Query("""
    SELECT r FROM FinancialRecord r
    WHERE r.userId = :userId
    AND (:type IS NULL OR r.type = :type)
    AND (:categoryId IS NULL OR r.category.id = :categoryId)
    AND (:startDate IS NULL OR r.date >= :startDate)
    AND (:endDate IS NULL OR r.date <= :endDate)
""")
    Page<FinancialRecord> findByFilters(
            Long userId,
            String type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}