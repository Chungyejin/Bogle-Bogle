package com.bogle.ingredientvillage.service;

import com.bogle.ingredientvillage.entity.Ingredient;
import com.bogle.ingredientvillage.entity.Recipe;
import com.bogle.ingredientvillage.entity.RecipeIngredient;
import com.bogle.ingredientvillage.repository.IngredientRepository;
import com.bogle.ingredientvillage.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KitchenService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public KitchenService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    // [기능 2-1] 사용자가 냉장고에 재료 추가
    @Transactional
    public Ingredient addIngredientToFridge(String name, int quantity) {
        Optional<Ingredient> existingIngredient = ingredientRepository.findByName(name);

        if (existingIngredient.isPresent()) {
            // 이미 존재하는 재료면 수량 추가
            Ingredient ingredient = existingIngredient.get();
            ingredient.setQuantity(ingredient.getQuantity() + quantity);
            return ingredientRepository.save(ingredient);
        } else {
            // 새 재료면 저장
            Ingredient ingredient = new Ingredient(name, quantity);
            return ingredientRepository.save(ingredient);
        }
    }

    // [기능 2-2] 냉장고 속 재료 전체 조회
    public List<Ingredient> getAllFridgeIngredients() {
        return ingredientRepository.findAll();
    }

    // [기능 2-2] 선택한 재료로 요리 가능한 레시피 조회 (OR 조건: 선택한 재료 중 하나라도 포함시 추천)
    public List<Recipe> findRecipesBySelectedIngredients(List<String> selectedIngredients) {
        List<Recipe> allRecipes = recipeRepository.findAll();
        List<Recipe> recommendedRecipes = new ArrayList<>();

        if (selectedIngredients == null || selectedIngredients.isEmpty()) {
            return recommendedRecipes;
        }

        for (Recipe recipe : allRecipes) {
            // 해당 레시피에 필요한 재료 이름 목록 추출
            List<String> requiredNames = recipe.getRecipeIngredients().stream()
                    .map(RecipeIngredient::getName)
                    .toList();

            // 선택한 재료(selectedIngredients) 중 하나라도 레시피 재료에 포함되어 있는지 확인
            boolean isMatch = selectedIngredients.stream()
                    .anyMatch(requiredNames::contains);

            if (isMatch) {
                recommendedRecipes.add(recipe);
            }
        }

        return recommendedRecipes;
    }

    // [기능 2-3] 레시피 활용 시 필요 수량 차감 (부족한 재료 수량 사전 검증 포함)
    @Transactional
    public String useRecipe(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) {
            return "해당 레시피를 찾을 수 없습니다.";
        }

        Recipe recipe = recipeOptional.get();

        // 1. 사전 검사: 부족한 재료 목록을 담을 리스트 생성
        List<String> missingIngredients = new ArrayList<>();

        for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
            Optional<Ingredient> fridgeIngOptional = ingredientRepository.findByName(ri.getName());

            if (fridgeIngOptional.isEmpty()) {
                // 냉장고에 재료가 아예 없는 경우
                missingIngredients.add(ri.getName() + " (필요: " + ri.getRequiredQuantity() + "개, 보유: 0개)");
            } else {
                Ingredient fridgeIng = fridgeIngOptional.get();
                if (fridgeIng.getQuantity() < ri.getRequiredQuantity()) {
                    // 재료는 있지만 수량이 부족한 경우
                    int shortage = ri.getRequiredQuantity() - fridgeIng.getQuantity();
                    missingIngredients.add(ri.getName() + " (" + shortage + "개 부족)");
                }
            }
        }

        // 2. 검증 조건문: 부족한 재료가 하나라도 있으면 차감하지 않고 안내 메시지 반환
        if (!missingIngredients.isEmpty()) {
            return "재료가 부족하여 요리를 할 수 없습니다.\n- 부족한 재료: " + String.join(", ", missingIngredients);
        } else {
            // 3. 재료가 모두 충분할 때만 실제 차감 진행
            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                Ingredient fridgeIng = ingredientRepository.findByName(ri.getName()).get();
                int remainQty = fridgeIng.getQuantity() - ri.getRequiredQuantity();

                if (remainQty <= 0) {
                    // 수량이 0 이하면 삭제
                    ingredientRepository.delete(fridgeIng);
                } else {
                    // 수량 감소 저장
                    fridgeIng.setQuantity(remainQty);
                    ingredientRepository.save(fridgeIng);
                }
            }

            return recipe.getName() + " 요리가 완료되어 재료가 차감되었습니다.";
        }
    }

    // [테스트용] 샘플 레시피 직접 등록 API
    @Transactional
    public Recipe createSampleRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
}