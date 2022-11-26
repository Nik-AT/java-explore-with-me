package ru.practicum.ewm.categories.model;

import ru.practicum.ewm.categories.model.dto.CategoryDto;

public class CategoryMapper {

    public static CategoryDto toDto(Category c) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(c.getCategoryId());
        categoryDto.setName(c.getCategoryName());
        return categoryDto;
    }
}
