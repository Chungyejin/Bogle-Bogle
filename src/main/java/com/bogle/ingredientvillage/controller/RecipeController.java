package com.bogle.ingredientvillage.controller;

import com.bogle.ingredientvillage.dto.RecipeResponse;
import com.bogle.ingredientvillage.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 👈 플러터 웹(CORS) 요청 허용 설정
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/match")
    public ResponseEntity<List<RecipeResponse>> getRecipesByIngredients(
            @RequestParam("ingredientIds") List<Long> ingredientIds) {

        List<RecipeResponse> responses = recipeService.getRecipesByIngredients(ingredientIds)
                .stream()
                .map(RecipeResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}