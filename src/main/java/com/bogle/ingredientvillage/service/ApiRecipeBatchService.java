package com.bogle.ingredientvillage.service;

import com.bogle.ingredientvillage.domain.Ingredient;
import com.bogle.ingredientvillage.domain.Recipe;
import com.bogle.ingredientvillage.repository.RecipeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ApiRecipeBatchService {

    private final RecipeRepository recipeRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.service-key}")
    private String serviceKey;

    @Transactional
    public void fetchAndSaveRecipes(int startIdx, int endIdx) {
        String url = String.format("http://openapi.foodsafetykorea.go.kr/api/%s/COOKRCP01/json/%d/%d",
                serviceKey, startIdx, endIdx);

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode rowNode = root.path("COOKRCP01").path("row");

            if (rowNode.isArray()) {
                for (JsonNode node : rowNode) {
                    String apiRecipeId = node.path("RCP_SEQ").asText();

                    if (recipeRepository.existsByApiRecipeId(apiRecipeId)) {
                        continue;
                    }

                    // 조리 과정 MANUAL01 ~ MANUAL20 합치기
                    StringBuilder manualBuilder = new StringBuilder();
                    for (int i = 1; i <= 20; i++) {
                        String key = String.format("MANUAL%02d", i);
                        String step = node.path(key).asText();
                        if (step != null && !step.isBlank()) {
                            manualBuilder.append(step.replace("\n", "")).append("\n");
                        }
                    }

                    Recipe recipe = Recipe.builder()
                            .apiRecipeId(apiRecipeId)
                            .description(manualBuilder.toString().trim())
                            .build();

                    // 재료 파싱 (이름, 수량)
                    String rawParts = node.path("RCP_PARTS_DTLS").asText();
                    parseAndAddIngredients(recipe, rawParts);

                    recipeRepository.save(recipe);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseAndAddIngredients(Recipe recipe, String rawParts) {
        if (rawParts == null || rawParts.isBlank()) return;

        String[] items = rawParts.split("[,\\n\\r]+");
        for (String item : items) {
            String trimmed = item.trim();
            if (trimmed.isEmpty()) continue;

            String[] parts = trimmed.split("\\s+", 2);
            String name = parts[0].replaceAll("[0-9gml개큰술작은술약간\\(\\)]", "").trim();
            String quantity = parts.length > 1 ? parts[1].trim() : "적당량";

            if (!name.isEmpty()) {
                Ingredient ingredient = Ingredient.builder()
                        .name(name)
                        .quant(quantity)
                        .build();
                recipe.addIngredient(ingredient);
            }
        }
    }
}