package springboot.catchshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import springboot.catchshop.domain.Cart;
import springboot.catchshop.domain.Product;

/**
 * Cart Request Dto (저장시 장바구니 정보를 담고 있는 Dto)
 * author: soohyun, last modified: 22.03.05
 */

@Data
@AllArgsConstructor
public class CartRequestDto {

    private Long userId; // 로그인한 사용자 번호
    private Product product; // 상품
    //private int cartCount; // 상품 수량

    public Cart toEntity() {
        return new Cart(product, userId);
    }
}
