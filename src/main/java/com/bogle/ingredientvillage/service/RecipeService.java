package com.bogle.ingredientvillage.service;

import com.bogle.ingredientvillage.domain.Recipe;
import com.bogle.ingredientvillage.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {


    private final RecipeRepository recipeRepository;

    /**
     * 선택한 재료들로 만들 수 있는 레시피 목록 조회
     */
    public List<Recipe> getRecipesByIngredients(List<Long> ingredientIds) {
        // 재료 선택을 안 했을 경우 빈 리스트 반환
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return List.of();
        }

        return recipeRepository.findRecipesByIngredientIds(ingredientIds);
    }
}