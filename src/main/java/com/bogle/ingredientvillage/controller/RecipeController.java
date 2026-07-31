package com.bogle.ingredientvillage.controller;

import com.bogle.ingredientvillage.dto.RecipeResponseDto;
import com.bogle.ingredientvillage.service.ApiRecipeBatchService;
import com.bogle.ingredientvillage.service.RecipeSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeSearchService recipeSearchService;
    private final ApiRecipeBatchService apiRecipeBatchService;

    // 보유 재료 기반 레시피 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponseDto>> searchRecipes(@RequestParam List<String> ingredients) {
        List<RecipeResponseDto> results = recipeSearchService.searchByIngredients(ingredients);
        return ResponseEntity.ok(results);
    }

    // 공공데이터 DB 수집 (관리용 API)
    @PostMapping("/sync")
    public ResponseEntity<String> syncRecipes(@RequestParam(defaultValue = "1") int start,
                                              @RequestParam(defaultValue = "100") int end) {
        apiRecipeBatchService.fetchAndSaveRecipes(start, end);
        return ResponseEntity.ok("공공데이터 레시피 수집이 완료되었습니다.");
    }
}