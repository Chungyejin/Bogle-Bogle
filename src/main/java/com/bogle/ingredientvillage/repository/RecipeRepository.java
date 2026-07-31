package com.bogle.ingredientvillage.repository;

import com.bogle.ingredientvillage.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // 1. 공공 API 중복 저장 방지용 메서드
    boolean existsByApiRecipeId(String apiRecipeId);

    // 2. 재료 이름 리스트로 레시피 검색하는 쿼리
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "JOIN r.ingredients i " +
            "WHERE i.name IN :ingredients")
    List<Recipe> findRecipesByIngredients(@Param("ingredients") List<String> ingredients);
}