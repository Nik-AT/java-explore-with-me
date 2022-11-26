package ru.practicum.ewm.categories.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "categories")
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;

        Category c = (Category) o;
        return this.categoryId.equals(c.getCategoryId()) && this.categoryName.equals(c.getCategoryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryName);
    }
}
