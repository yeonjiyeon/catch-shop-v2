package springboot.catchshop.domain;

import lombok.Getter;

@Getter
public enum QuestionCategory {
    PRODUCT("상품 문의"), DELIVERY("배송 문의"), ORDER("주문 문의");

    private final String description;

    QuestionCategory(String description) {
        this.description = description;
    }
}