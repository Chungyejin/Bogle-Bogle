package com.bogle.ingredientvillage.repository;


import com.bogle.ingredientvillage.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}