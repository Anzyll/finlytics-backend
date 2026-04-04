package com.anzil.finlytics.record.service;

import com.anzil.finlytics.common.exception.AppException;
import com.anzil.finlytics.common.exception.ErrorCode;
import com.anzil.finlytics.record.dto.*;
import com.anzil.finlytics.record.entity.*;
import com.anzil.finlytics.record.mapper.FinancialRecordMapper;
import com.anzil.finlytics.record.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepo;
    private final CategoryRepository categoryRepo;
    private final FinancialRecordMapper mapper;

    public FinancialRecordResponse create(FinancialRecordCreateRequest req, Long userId) {

        Category category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        FinancialRecord record = mapper.toEntity(req, category, userId);

        return mapper.toResponse(recordRepo.save(record));
    }

    public Page<FinancialRecordResponse> getFilteredRecords(
            Long userId,
            String type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size

    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<FinancialRecord> records =
                recordRepo.findByFilters(userId, type, categoryId, startDate, endDate, pageable);

        return records.map(mapper::toResponse);
    }

    public FinancialRecordResponse update(Long id, FinancialRecordUpdateRequest req, Long userId) {

        FinancialRecord record = recordRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_FOUND));

        if (!record.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        if (req.amount() != null) record.setAmount(req.amount());
        if (req.type() != null) record.setType(req.type());

        if (req.categoryId() != null) {
            Category category = categoryRepo.findById(req.categoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            record.setCategory(category);
        }
        if (req.date() != null) record.setDate(req.date());
        if (req.notes() != null) record.setNotes(req.notes());

        return mapper.toResponse(recordRepo.save(record));
    }

    public void delete(Long id, Long userId) {

        FinancialRecord record = recordRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_FOUND));

        if (!record.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        recordRepo.delete(record);
    }
}
