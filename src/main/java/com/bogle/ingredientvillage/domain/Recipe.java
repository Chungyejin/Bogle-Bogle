package com.bogle.ingredientvillage.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe") // 앞서 대소문자 이슈 방지를 위해 소문자로 맞추기로 했던 부분도 반영했습니다!
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 🔥 이 연관관계 설정이 추가되어야 Repository의 쿼리가 정상 작동합니다!
    // RecipeIngredient 엔티티에 있는 'recipe' 필드에 의해 매핑되었다는 뜻입니다.
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private final List<RecipeIngredient> recipeIngredients = new ArrayList<>();
}