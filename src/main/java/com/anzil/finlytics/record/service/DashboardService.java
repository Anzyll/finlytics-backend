package com.anzil.finlytics.record.service;

import com.anzil.finlytics.record.dto.*;
import com.anzil.finlytics.record.mapper.FinancialRecordMapper;
import com.anzil.finlytics.record.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordRepository repo;
    private final FinancialRecordMapper mapper;

    public DashboardSummaryResponse getSummary(Long userId) {

        BigDecimal income = repo.getTotalIncome(userId);
        BigDecimal expense = repo.getTotalExpense(userId);

        return new DashboardSummaryResponse(
                income,
                expense,
                income.subtract(expense)
        );
    }

    public List<CategorySummaryResponse> getCategorySummary(Long userId) {
        return repo.getCategorySummary(userId);
    }

    public List<FinancialRecordResponse> getRecent(Long userId) {

        return repo.findTop5ByUserIdOrderByDateDesc(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
    public InsightResponse getInsights(Long userId) {

        List<Object[]> list = repo.getTopExpenseCategoryList(userId);
        Object[] top = list.isEmpty() ? null : list.get(0);

        String category = top != null ? (String) top[0] : "N/A";
        BigDecimal topAmount = top != null ? (BigDecimal) top[1] : BigDecimal.ZERO;

        BigDecimal totalExpense = repo.getTotalExpense(userId);

        BigDecimal percentage = BigDecimal.ZERO;

        if (totalExpense.compareTo(BigDecimal.ZERO) > 0) {
            percentage = topAmount
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalExpense, 2, RoundingMode.HALF_UP);
        }

        LocalDate now = LocalDate.now();

        LocalDate currentStart = now.withDayOfMonth(1);
        LocalDate currentEnd = now;

        LocalDate prevStart = currentStart.minusMonths(1);
        LocalDate prevEnd = currentStart.minusDays(1);

        BigDecimal current = repo.getExpenseBetween(userId, currentStart, currentEnd);
        BigDecimal previous = repo.getExpenseBetween(userId, prevStart, prevEnd);

        String trend;

        if (current.compareTo(previous) > 0) {
            trend = "increase";
        } else if (current.compareTo(previous) < 0) {
            trend = "decrease";
        } else {
            trend = "same";
        }

        return new InsightResponse(category, percentage, trend);
    }
}