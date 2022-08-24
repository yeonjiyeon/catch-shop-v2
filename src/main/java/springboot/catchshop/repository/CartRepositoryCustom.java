package springboot.catchshop.repository;

import springboot.catchshop.domain.Cart;

import java.util.List;

/**
 * Cart Repository Custom
 * author: soohyun, last modified: 22.03.26
 */
public interface CartRepositoryCustom {

    // 로그인한 사용자 id로 장바구니 목록 조회
    List<Cart> cartList(Long userId);

    // 로그인한 사용자 id로 주문 가능한 장바구니 목록 조회
    List<Cart> orderCartList(Long userId);
}
