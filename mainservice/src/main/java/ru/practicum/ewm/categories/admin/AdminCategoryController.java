package ru.practicum.ewm.categories.admin;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.model.CategoryMapper;
import ru.practicum.ewm.categories.model.dto.CategoryDto;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public CategoryDto addCategory(@RequestBody CategoryDto categoryDto) {
        if (categoryDto.getName() == null) throw new RuntimeException("Wrong Dto Body!");
        return CategoryMapper.toDto(adminCategoryService.addNewCategory(categoryDto.getName()));
    }

    @PatchMapping
    public CategoryDto changeCategory(@RequestBody CategoryDto categoryDto) {
        return CategoryMapper.toDto(adminCategoryService.changeCategory(categoryDto));
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        adminCategoryService.deleteCategory(catId);
    }
}
