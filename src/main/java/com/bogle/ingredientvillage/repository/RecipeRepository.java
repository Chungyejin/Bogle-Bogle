package com.bogle.ingredientvillage.repository;

import com.bogle.ingredientvillage.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // 사용자가 가진 재료 ID 리스트에 해당하는 레시피들을 중복 없이 조회합니다.
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "JOIN r.recipeIngredients ri " +
            "WHERE ri.ingredient.id IN :ingredientIds")
    List<Recipe> findRecipesByIngredientIds(@Param("ingredientIds") List<Long> ingredientIds);
}