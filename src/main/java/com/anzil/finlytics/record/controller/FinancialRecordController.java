package com.anzil.finlytics.record.controller;


import com.anzil.finlytics.record.dto.*;
import com.anzil.finlytics.record.service.FinancialRecordService;
import com.anzil.finlytics.security.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService service;

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody FinancialRecordCreateRequest req
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(service.create(req, userId));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(service.getAll(userId));
    }

    @PatchMapping("/{id}") // 🔥 better for partial update
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody FinancialRecordUpdateRequest req
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(service.update(id, req, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Long userId = SecurityUtil.getCurrentUserId();
        service.delete(id, userId);

        return ResponseEntity.ok("Deleted successfully");
    }
}