package springboot.catchshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.catchshop.domain.Cart;
import springboot.catchshop.domain.Product;

import java.util.List;

/**
 * Cart Repository
 * author: soohyun, last modified: 22.04.06
 */

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {

    // 로그인한 사용자 id로 해당 상품이 담긴 장바구니 조회
    Cart findByUserIdAndProduct(Long userId, Product product);
}
