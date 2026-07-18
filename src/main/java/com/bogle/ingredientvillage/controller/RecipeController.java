package com.bogle.ingredientvillage.controller;

import com.bogle.ingredientvillage.dto.RecipeResponse; // 추가
import com.bogle.ingredientvillage.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/match")
    public ResponseEntity<List<RecipeResponse>> getRecipesByIngredients(
            @RequestParam("ingredientIds") List<Long> ingredientIds) {

        // 서비스에서 가져온 엔티티 리스트를 DTO 리스트로 깔끔하게 변환합니다.
        List<RecipeResponse> responses = recipeService.getRecipesByIngredients(ingredientIds)
                .stream()
                .map(RecipeResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}