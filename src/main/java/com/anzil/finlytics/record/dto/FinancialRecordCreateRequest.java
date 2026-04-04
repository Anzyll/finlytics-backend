package com.anzil.finlytics.record.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialRecordCreateRequest(

        @NotNull @Positive
        BigDecimal amount,

        @NotBlank
        String type,

        @NotNull
        Long categoryId,

        @NotNull
        LocalDate date,

        String notes
) {}
