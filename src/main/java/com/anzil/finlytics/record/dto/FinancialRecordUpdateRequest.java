package com.anzil.finlytics.record.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialRecordUpdateRequest(

        BigDecimal amount,
        String type,
        Long categoryId,
        LocalDate date,
        String notes
) {}