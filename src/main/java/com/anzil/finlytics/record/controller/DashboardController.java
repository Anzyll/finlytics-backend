package com.anzil.finlytics.record.controller;

import com.anzil.finlytics.record.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Dashboard & Analytics")
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<?> getSummary() {
        return ResponseEntity.ok(service.getSummary());
    }

    @GetMapping("/category")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<?> getCategorySummary() {
        return ResponseEntity.ok(service.getCategorySummary());
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ANALYST','ADMIN')")
    public ResponseEntity<?> getRecent() {
        return ResponseEntity.ok(service.getRecent());
    }

    @GetMapping("/insights")
    @PreAuthorize("hasAnyRole('ANALYST','ADMIN')")
    public ResponseEntity<?> getInsights() {
        return ResponseEntity.ok(service.getInsights());
    }
}