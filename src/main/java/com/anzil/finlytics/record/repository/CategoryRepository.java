package com.anzil.finlytics.record.repository;


import com.anzil.finlytics.record.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}