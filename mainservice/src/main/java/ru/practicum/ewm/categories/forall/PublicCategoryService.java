package ru.practicum.ewm.categories.forall;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.categories.CategoryRepository;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.exceptions.ModelNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PublicCategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getWithPagination(int from, int size) {
        return categoryRepository.getWithPagination(from, size);
    }

    public Category getCategory(Long catId) {
        Optional<Category> optionalCategory = categoryRepository.findById(catId);
        if (optionalCategory.isEmpty()) throw new ModelNotFoundException("Category not found!");
        return optionalCategory.get();
    }
}
