package com.anzil.finlytics.record.controller;

import com.anzil.finlytics.record.service.DashboardService;
import com.anzil.finlytics.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary() {

        Long userId = SecurityUtil.getCurrentUserId();

        return ResponseEntity.ok(service.getSummary(userId));
    }

    @GetMapping("/category")
    public ResponseEntity<?> getCategorySummary() {

        Long userId = SecurityUtil.getCurrentUserId();

        return ResponseEntity.ok(service.getCategorySummary(userId));
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecent() {

        Long userId = SecurityUtil.getCurrentUserId();

        return ResponseEntity.ok(service.getRecent(userId));
    }
    @GetMapping("/insights")
    public ResponseEntity<?> getInsights() {

        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(service.getInsights(userId));
    }
}