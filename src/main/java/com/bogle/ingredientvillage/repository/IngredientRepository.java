package com.bogle.ingredientvillage.repository;

import com.bogle.ingredientvillage.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<엔티티클래스, PK타입>을 상속받으면 기본적인 save(), findAll(), findById() 등을 바로 사용 가능
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

        // 재료 이름으로 냉장고 데이터 찾기
        Optional<Ingredient> findByName(String name);
}
