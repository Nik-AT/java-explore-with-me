package ru.practicum.ewm.categories.admin;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.categories.CategoryRepository;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.model.dto.CategoryDto;
import ru.practicum.ewm.exceptions.ModelAlreadyExistsException;
import ru.practicum.ewm.exceptions.ModelNotFoundException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;

    public Category addNewCategory(String categoryName) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ModelAlreadyExistsException("Category name must be unique!");
        }
    }

    public Category changeCategory(CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getId());
        if (optionalCategory.isEmpty()) {
            throw new ModelNotFoundException("Category not found!");
        } else {
            Category category = optionalCategory.get();
            category.setCategoryName(categoryDto.getName());
            try {
                return categoryRepository.save(category);
            } catch (DataIntegrityViolationException e) {
                throw new ModelAlreadyExistsException("This name is used!");
            }
        }
    }

    public void deleteCategory(Long catId) {
        try {
            categoryRepository.deleteById(catId);
        } catch (EmptyResultDataAccessException e) {
            throw new ModelNotFoundException("Category not found!");
        }
    }
}
