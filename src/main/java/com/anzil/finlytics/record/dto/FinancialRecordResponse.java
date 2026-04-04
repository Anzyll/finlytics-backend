package com.anzil.finlytics.record.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialRecordResponse(

        Long id,
        BigDecimal amount,
        String type,
        String categoryName,
        LocalDate date,
        String notes
) {}