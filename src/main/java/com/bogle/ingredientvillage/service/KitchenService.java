package com.bogle.ingredientvillage.service;

import com.bogle.ingredientvillage.entity.Recipe;
import com.bogle.ingredientvillage.entity.Ingredient;
import com.bogle.ingredientvillage.entity.RecipeIngredient;
import com.bogle.ingredientvillage.repository.IngredientRepository;
import com.bogle.ingredientvillage.repository.RecipeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service // 이 클래스가 비즈니스 로직을 담당하는 서비스 클래스임을 스프링에 등록
public class KitchenService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    // 생성자 주입: 리포지토리들을 스프링으로부터 전달받아 사용합니다.
    public KitchenService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    // 기능 2-1: 냉장고에 재료 추가하기
    public Ingredient addIngredientToFridge(String name, int quantity) {
        // 이미 냉장고에 동일한 재료가 있는지 찾습니다.
        Optional<Ingredient> existing = ingredientRepository.findByName(name);

        if (existing.isPresent()) {
            // 이미 존재한다면 기존 수량에 더해줍니다.
            Ingredient ingredient = existing.get();
            ingredient.setQuantity(ingredient.getQuantity() + quantity);
            return ingredientRepository.save(ingredient);
        } else {
            // 냉장고에 처음 들어가는 재료라면 새로 생성하여 저장합니다.
            Ingredient newIngredient = new Ingredient(name, quantity);
            return ingredientRepository.save(newIngredient);
        }
    }

    // 기능 2-2 핵심: 냉장고 속 전체 재료 목록 가져오기 (부엌 입장 시)
    public List<Ingredient> getAllFridgeIngredients() {
        return ingredientRepository.findAll();
    }

    // 기능 2-2 핵심: 사용자가 선택한 재료 이름들을 가지고 만들 수 있는 레시피 추천
    public List<Recipe> findRecipesBySelectedIngredients(List<String> selectedNames) {
        List<Recipe> allRecipes = recipeRepository.findAll();
        List<Recipe> possibleRecipes = new ArrayList<>();

        // 등록된 모든 레시피를 하나씩 점검합니다.
        for (Recipe recipe : allRecipes) {
            boolean canMake = true;

            // 레시피에 필요한 재료 목록을 확인
            for (RecipeIngredient req : recipe.getRecipeIngredients()) {
                // 선택한 재료 이름 중에 레시피 재료가 포함되어 있는지 확인
                if (!selectedNames.contains(req.getName())) {
                    canMake = false; // 하나라도 없으면 만들 수 없음
                    break;
                }
            }

            // 필요한 재료가 모두 선택에 포함되어 있다면 추천 목록에 추가
            if (canMake && !recipe.getRecipeIngredients().isEmpty()) {
                possibleRecipes.add(recipe);
            }
        }

        return possibleRecipes;
    }

    // 기능 2-3: 레시피 사용 시, 필요 수량만큼 냉장고 재료 차감
    @Transactional // 메서드가 성공적으로 완료되면 DB 변경사항을 커밋합니다.
    public String useRecipe(Long recipeId) {
        // ID로 레시피 조회 (없으면 예외 발생)
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레시피입니다. ID: " + recipeId));

        // 1차 점검: 모든 재료 수량이 충분한지 미리 검사
        for (RecipeIngredient req : recipe.getRecipeIngredients()) {
            Ingredient fridgeItem = ingredientRepository.findByName(req.getName())
                    .orElseThrow(() -> new RuntimeException("냉장고에 [" + req.getName() + "] 재료가 없습니다."));

            if (fridgeItem.getQuantity() < req.getRequiredQuantity()) {
                return "실패: [" + req.getName() + "] 수량이 부족합니다. (필요: "
                        + req.getRequiredQuantity() + ", 보유: " + fridgeItem.getQuantity() + ")";
            }
        }

        // 2차 차감: 실제로 냉장고 재료 수량을 차감
        for (RecipeIngredient req : recipe.getRecipeIngredients()) {
            Ingredient fridgeItem = ingredientRepository.findByName(req.getName()).get();
            fridgeItem.setQuantity(fridgeItem.getQuantity() - req.getRequiredQuantity());

            // 수량이 0이 되면 냉장고 데이터에서 완전히 삭제 (또는 수량 0으로 유지)
            if (fridgeItem.getQuantity() <= 0) {
                ingredientRepository.delete(fridgeItem);
            } else {
                ingredientRepository.save(fridgeItem);
            }
        }

        return "성공: [" + recipe.getName() + "] 요리를 진행하여 재료를 차감했습니다!";
    }

    // 초기 테스트용 샘플 레시피 등록 기능
    public Recipe createSampleRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
}