package com.anzil.finlytics.record.controller;


import com.anzil.finlytics.record.dto.*;
import com.anzil.finlytics.record.service.FinancialRecordService;
import com.anzil.finlytics.security.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Tag(name = "Financial Records")
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> create(
            @Valid @RequestBody FinancialRecordCreateRequest req
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(service.create(req, userId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<?> getRecords(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        return ResponseEntity.ok(
                service.getFilteredRecords(type, categoryId, startDate, endDate,search,page,size)
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody FinancialRecordUpdateRequest req
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(service.update(id, req, userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        service.delete(id, userId);
        return ResponseEntity.ok("Deleted successfully");
    }
}