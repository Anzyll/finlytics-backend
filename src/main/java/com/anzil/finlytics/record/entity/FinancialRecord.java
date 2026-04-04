package com.anzil.finlytics.record.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private BigDecimal amount;
    private String type;
    private LocalDate date;
    private String notes;
    private LocalDateTime createdAt;
}