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

    public DashboardSummaryResponse getSummary() {

        BigDecimal income = repo.getTotalIncome();
        BigDecimal expense = repo.getTotalExpense();

        return new DashboardSummaryResponse(
                income,
                expense,
                income.subtract(expense)
        );
    }

    public List<CategorySummaryResponse> getCategorySummary() {
        return repo.getCategorySummary();
    }


    public List<FinancialRecordResponse> getRecent() {

        return repo.findTop5ByOrderByDateDesc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    public InsightResponse getInsights() {

        List<TopCategoryResponse> list = repo.getTopExpenseCategory();
        TopCategoryResponse top = list.isEmpty()
                ? new TopCategoryResponse("N/A", BigDecimal.ZERO)
                : list.get(0);

        String category = top.category();
        BigDecimal topAmount = top.total();

        BigDecimal totalExpense = repo.getTotalExpense();

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

        BigDecimal current = repo.getExpenseBetweenGlobal(currentStart, currentEnd);
        BigDecimal previous = repo.getExpenseBetweenGlobal(prevStart, prevEnd);

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