package com.bogle.ingredientvillage.entity;

import jakarta.persistence.*;

// @Entity: 이 클래스가 데이터베이스의 테이블과 1:1로 매핑되는 객체임을 스프링에 알립니다.
@Entity
public class Ingredient {

    // @Id: 데이터베이스의 Primary Key(기본키)임을 지정합니다.
    // @GeneratedValue: ID 값을 자동으로 1씩 증가시키며 생성합니다 (AUTO_INCREMENT).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 재료 이름 (예: "계란", "양파")
    private String name;

    // 재료 수량 (예: 5)
    private int quantity;

    // 기본 생성자 (JPA 사용을 위해 반드시 필요합니다)
    public Ingredient() {}

    // 필드가 있는 생성자 (객체를 편하게 만들기 위해 추가)
    public Ingredient(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // --- Getter / Setter (데이터를 읽고 수정하기 위한 메서드들) ---

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}