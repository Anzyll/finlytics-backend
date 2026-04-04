package com.anzil.finlytics.record.repository;

import com.anzil.finlytics.record.dto.CategorySummaryResponse;
import com.anzil.finlytics.record.entity.FinancialRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
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

    @Query("""
        SELECT COALESCE(SUM(r.amount), 0)
        FROM FinancialRecord r
        WHERE r.userId = :userId AND r.type = 'INCOME'
    """)
    BigDecimal getTotalIncome(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(SUM(r.amount), 0)
        FROM FinancialRecord r
        WHERE r.userId = :userId AND r.type = 'EXPENSE'
    """)
    BigDecimal getTotalExpense(@Param("userId") Long userId);

    @Query("""
        SELECT new com.anzil.finlytics.record.dto.CategorySummaryResponse(
            r.category.name,
            SUM(r.amount)
        )
        FROM FinancialRecord r
        WHERE r.userId = :userId
        GROUP BY r.category.name
    """)
    List<CategorySummaryResponse> getCategorySummary(@Param("userId") Long userId);

    List<FinancialRecord> findTop5ByUserIdOrderByDateDesc(Long userId);

    @Query("""
        SELECT r.category.name, SUM(r.amount) as total
        FROM FinancialRecord r
        WHERE r.userId = :userId AND r.type = 'EXPENSE'
        GROUP BY r.category.name
        ORDER BY total DESC
    """)
    List<Object[]> getTopExpenseCategoryList(@Param("userId") Long userId);

    @Query("""
    SELECT COALESCE(SUM(r.amount), 0)
    FROM FinancialRecord r
    WHERE r.userId = :userId
    AND r.type = 'EXPENSE'
    AND r.date BETWEEN :start AND :end
""")
    BigDecimal getExpenseBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}
