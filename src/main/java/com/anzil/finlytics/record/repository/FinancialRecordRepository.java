package com.anzil.finlytics.record.repository;

import com.anzil.finlytics.record.dto.*;
import com.anzil.finlytics.record.entity.FinancialRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    @Query("""
        SELECT r FROM FinancialRecord r
        WHERE r.userId = :userId
        AND (:type IS NULL OR r.type = :type)
        AND (:categoryId IS NULL OR r.category.id = :categoryId)
        AND (:startDate IS NULL OR r.date >= :startDate)
        AND (:endDate IS NULL OR r.date <= :endDate)
        AND (
            :search IS NULL OR
            LOWER(COALESCE(r.notes, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(r.category.name) LIKE LOWER(CONCAT('%', :search, '%'))
        )
    """)
    Page<FinancialRecord> findByFilters(
            Long userId,
            String type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            String search,
            Pageable pageable
    );

    @Query("""
        SELECT COALESCE(SUM(r.amount), 0)
        FROM FinancialRecord r
        WHERE r.type = 'INCOME'
    """)
    BigDecimal getTotalIncome();


    @Query("""
        SELECT COALESCE(SUM(r.amount), 0)
        FROM FinancialRecord r
        WHERE r.type = 'EXPENSE'
    """)
    BigDecimal getTotalExpense();


    @Query("""
        SELECT new com.anzil.finlytics.record.dto.CategorySummaryResponse(
            r.category.name,
            SUM(r.amount)
        )
        FROM FinancialRecord r
        GROUP BY r.category.name
    """)
    List<CategorySummaryResponse> getCategorySummary();


    @Query("""
        SELECT new com.anzil.finlytics.record.dto.TopCategoryResponse(
            r.category.name,
            SUM(r.amount)
        )
        FROM FinancialRecord r
        WHERE r.type = 'EXPENSE'
        GROUP BY r.category.name
        ORDER BY SUM(r.amount) DESC
    """)
    List<TopCategoryResponse> getTopExpenseCategory();

    @Query("""
    SELECT COALESCE(SUM(r.amount), 0)
    FROM FinancialRecord r
    WHERE r.type = 'EXPENSE'
    AND r.date BETWEEN :start AND :end
""")
    BigDecimal getExpenseBetweenGlobal(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    List<FinancialRecord> findTop5ByOrderByDateDesc();
}