package com.anzil.finlytics.record.mapper;

import com.anzil.finlytics.record.dto.*;
import com.anzil.finlytics.record.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FinancialRecordMapper {

    public FinancialRecord toEntity(
            FinancialRecordCreateRequest req,
            Category category,
            Long userId
    ) {
        return FinancialRecord.builder()
                .userId(userId)
                .category(category)
                .amount(req.amount())
                .type(req.type())
                .date(req.date())
                .notes(req.notes())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public FinancialRecordResponse toResponse(FinancialRecord record) {
        return new FinancialRecordResponse(
                record.getId(),
                record.getAmount(),
                record.getType(),
                record.getCategory().getName(),
                record.getDate(),
                record.getNotes()
        );
    }
}
