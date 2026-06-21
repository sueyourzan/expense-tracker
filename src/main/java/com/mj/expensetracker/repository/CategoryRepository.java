package com.mj.expensetracker.repository;

import com.mj.expensetracker.entity.Category;
import com.mj.expensetracker.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    List<Category> findByUserIdAndType(Long userId, CategoryType type);
    boolean existsByNameAndUserId(String name, Long userId);
}