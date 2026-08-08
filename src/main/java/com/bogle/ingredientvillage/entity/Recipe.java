package com.bogle.ingredientvillage.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 레시피 이름 (예: "계란말이")
    private String name;

    // 레시피 설명 (예: "달걀을 잘 풀어 프라이팬에 말아주는 요리")
    private String description;

    // @OneToMany: 하나의 레시피에 여러 개의 필요 재료가 연결됨
    // cascade = CascadeType.ALL: 레시피 저장/삭제 시 안의 재료 목록도 함께 처리됨
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    public Recipe() {}

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
}