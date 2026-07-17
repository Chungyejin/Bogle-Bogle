package com.bogle.ingredientvillage.service;

import org.junit.jupiter.api.Test;


import com.bogle.ingredientvillage.domain.Recipe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트가 끝나면 DB를 깨끗하게 롤백(되돌리기)해 줍니다.
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Test
    @DisplayName("선택한 재료 ID 리스트로 매칭되는 레시피를 성공적으로 조회한다")
    void getRecipesByIngredientsTest() {
        // given
        // 💡 테스트를 위해 MySQL Workbench에 임의로 넣으신 '재료 ID' 리스트를 적어줍니다.
        // 예를 들어 1번, 2번 재료를 선택했다고 가정합니다.
        List<Long> selectedIngredientIds = List.of(1L, 2L);

        // when
        List<Recipe> result = recipeService.getRecipesByIngredients(selectedIngredientIds);

        // then
        // 1. 결과가 널(Null)이 아니어야 하고
        assertThat(result).isNotNull();

        // 2. 콘솔창에 진짜로 DB에서 레시피를 잘 긁어왔는지 이름을 출력해 봅니다.
        System.out.println("====== [테스트 결과 확인] ======");
        for (Recipe recipe : result) {
            System.out.println("매칭된 레시피 이름: " + recipe.getName());
            System.out.println("레시피 설명: " + recipe.getDescription());
        }
        System.out.println("==============================");
    }
}