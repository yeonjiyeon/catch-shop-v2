package springboot.catchshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import springboot.catchshop.domain.Cart;

import javax.persistence.EntityManager;
import java.util.List;

import static springboot.catchshop.domain.QCart.cart;
import static springboot.catchshop.domain.QProduct.product;

/**
 * Cart Repository Impl
 * author: soohyun, last modified: 22.03.26
 */
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CartRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 로그인한 사용자 id로 장바구니 목록 조회
    public List<Cart> cartList(Long userId) {
        return queryFactory
                .selectFrom(cart)
                .join(cart.product, product).fetchJoin()
                .where(cart.userId.eq(userId))
                .fetch();
    }

    // 로그인한 사용자 id로 주문 가능한 장바구니 목록 조회
    public List<Cart> orderCartList(Long userId) {
        return queryFactory
                .selectFrom(cart)
                .join(cart.product, product).fetchJoin()
                .where(cart.userId.eq(userId), cart.cartCount.loe(product.stock))
                .fetch();
    }
}
