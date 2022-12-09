package ru.practicum.ewm.categories.forall;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.model.CategoryMapper;
import ru.practicum.ewm.categories.model.dto.CategoryDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class PublicCategoryController {
    private final PublicCategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getWithPagination(@RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return categoryService.getWithPagination(from, size)
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryInfo(@PathVariable Long catId) {
        return CategoryMapper.toDto(categoryService.getCategory(catId));
    }
}
