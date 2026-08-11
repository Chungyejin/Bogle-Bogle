package com.bogle.ingredientvillage.controller;

import com.bogle.ingredientvillage.entity.Ingredient;
import com.bogle.ingredientvillage.entity.Recipe;
import com.bogle.ingredientvillage.service.KitchenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST 규격(JSON 형태)으로 데이터를 주고받는 컨트롤러 지정
@RequestMapping("/api/kitchen") // 공통 기본 URL 주소 지정 (http://localhost:8080/api/kitchen)
public class KitchenController {

    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    // [기능 2-1] 사용자가 냉장고에 재료 추가
    // 요청 방식: POST http://localhost:8080/api/kitchen/ingredients
    // 전달 데이터: JSON {"name": "계란", "quantity": 10}
    @PostMapping("/ingredients")
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        return kitchenService.addIngredientToFridge(ingredient.getName(), ingredient.getQuantity());
    }

    // [기능 2-2] 부엌 파트 진입 시 냉장고 속 재료 전체 조회
    // 요청 방식: GET http://localhost:8080/api/kitchen/ingredients
    @GetMapping("/ingredients")
    public List<Ingredient> getFridgeIngredients() {
        return kitchenService.getAllFridgeIngredients();
    }

    // [기능 2-2] 선택한 재료로 요리 가능한 레시피 조회
    // 요청 방식: POST http://localhost:8080/api/kitchen/recipes/recommend
    @PostMapping("/recipes/recommend")
    public List<Recipe> recommendRecipes(@RequestBody List<String> selectedIngredients) {
        return kitchenService.findRecipesBySelectedIngredients(selectedIngredients);
    }

    // [기능 2-3] 레시피 활용 시 필요 수량 차감
    // 요청 방식: POST http://localhost:8080/api/kitchen/recipes/1/use
    @PostMapping("/recipes/{recipeId}/use")
    public String useRecipe(@PathVariable Long recipeId) {
        return kitchenService.useRecipe(recipeId);
    }

    // [테스트용] 샘플 레시피 직접 등록 API
    // 요청 방식: POST http://localhost:8080/api/kitchen/recipes
    @PostMapping("/recipes")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        // 연결 관계 매핑 설정
        if (recipe.getRecipeIngredients() != null) {
            recipe.getRecipeIngredients().forEach(ri -> ri.setRecipe(recipe));
        }
        return kitchenService.createSampleRecipe(recipe);
    }
}