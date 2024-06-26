package com.hits.forum.Core.Category.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @Size(min = 5, message = "Минимальная длина названия категории равна 5")
    @NotNull(message = "Минимальная длина названия категории равна 5")
    private String categoryName;

    private UUID parentId;
}
