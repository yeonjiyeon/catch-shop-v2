package springboot.catchshop.dto;

import lombok.Data;
import springboot.catchshop.domain.Cart;
import springboot.catchshop.domain.Product;

/**
 * Cart Info Dto (장바구니 정보를 담고 있는 Dto)
 * author: soohyun, last modified: 22.03.07
 */

@Data
public class CartInfoDto {

    private Long id;
    private Product product; // 상품
    private int cartCount; // 상품 수량
    private Long totalProductPrice; // 상품 총 가격
    private Boolean underStock; // 재고수량 초과 여부

    // 생성 메서드
    public CartInfoDto(Cart cart) {
        this.id = cart.getId();
        this.product = cart.getProduct();
        this.cartCount = cart.getCartCount();
        this.totalProductPrice = Long.valueOf(product.getPrice() * cartCount);
        this.underStock = cartCount <= cart.getProduct().getStock() ? true : false;
    }
}
