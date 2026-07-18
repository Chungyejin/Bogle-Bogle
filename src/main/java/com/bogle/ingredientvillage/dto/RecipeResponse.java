package com.bogle.ingredientvillage.dto;

import com.bogle.ingredientvillage.domain.Recipe;
import lombok.Getter;

@Getter
public class RecipeResponse {
    private Long id;
    private String name;
    private String description;

    // 엔티티를 DTO로 변환해주는 생성자이다.
    public RecipeResponse(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
    }
}