package com.bogle.ingredientvillage.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 필요한 재료의 이름 (예: "계란")
    private String name;

    // 레시피 완성에 필요한 수량 (예: 2개)
    private int requiredQuantity;

    // @ManyToOne: 여러 개의 재료가 하나의 레시피에 속할 수 있는 관계 설정
    // @JoinColumn: Recipe 테이블의 PK(id)를 외래키(FK)로 연결함
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore // 무한 루프(순환 참조) 방지를 위해 응답 JSON 변환 시 제외합니다.
    private Recipe recipe;

    public RecipeIngredient() {}

    public RecipeIngredient(String name, int requiredQuantity, Recipe recipe) {
        this.name = name;
        this.requiredQuantity = requiredQuantity;
        this.recipe = recipe;
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

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
