package com.mj.expensetracker.controller;

import com.mj.expensetracker.entity.Category;
import com.mj.expensetracker.entity.CategoryType;
import com.mj.expensetracker.entity.User;
import com.mj.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) CategoryType type) {
        if (type != null) {
            return ResponseEntity.ok(categoryRepository.findByUserIdAndType(user.getId(), type));
        }
        return ResponseEntity.ok(categoryRepository.findByUserId(user.getId()));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> body) {
        String name = body.get("name");
        CategoryType type = CategoryType.valueOf(body.get("type").toUpperCase());

        Category category = Category.builder()
                .name(name)
                .type(type)
                .user(user)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new com.mj.expensetracker.exception.ResourceNotFoundException("分类不存在: " + id));
        if (!category.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
