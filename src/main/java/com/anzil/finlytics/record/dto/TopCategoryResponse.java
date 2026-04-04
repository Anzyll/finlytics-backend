package com.anzil.finlytics.record.dto;
import java.math.BigDecimal;

public record TopCategoryResponse(
        String category,
        BigDecimal total
) {}