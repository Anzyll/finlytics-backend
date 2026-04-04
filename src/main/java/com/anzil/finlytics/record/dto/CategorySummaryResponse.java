package com.anzil.finlytics.record.dto;

import java.math.BigDecimal;

public record CategorySummaryResponse(
        String categoryName,
        BigDecimal total
) {}
