package com.anzil.finlytics.record.dto;
import java.math.BigDecimal;

public record InsightResponse(
        String topCategory,
        BigDecimal percentage,
        String trend
) {}