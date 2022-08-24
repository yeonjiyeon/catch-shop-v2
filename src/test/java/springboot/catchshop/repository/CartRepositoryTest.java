package springboot.catchshop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.Cart;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *  CartRepository Test
 *  author: soohyun, last modified: 22.04.06
 */

@SpringBootTest
@Transactional
class CartRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CartRepository cartRepository;

    private Address address;
    private User user;
    private Product product;

    @BeforeEach
    public void beforeEach() {
        // 사용자 생성
        address = new Address("road1", "detail1", "11111");
        user = new User("user1", "user1", "user1", "01012345678", "user1@catchshop.ac.kr", address, "USER");
        em.persist(user);

        // 상품 생성
        product = new Product();
        product.changePrice(10000);
        product.changeStock(10);
        em.persist(product);
    }

    @Nested
    @DisplayName("로그인한 사용자의 해당 상품이 담긴 장바구니 조회")
    class findByUserIdAndProduct {

        @Test
        @DisplayName("해당 상품이 장바구니에 있음")
        public void exist() {

            // given
            Cart cart = new Cart(product, user.getId(), 1);
            em.persist(cart);

            // when
            Cart findCart = cartRepository.findByUserIdAndProduct(user.getId(), product);

            // then
            assertEquals(findCart, cart);
        }

        @Test
        @DisplayName("해당 상품이 장바구니에 없음")
        public void not_exist() {

            // when
            Cart findCart = cartRepository.findByUserIdAndProduct(user.getId(), product);

            // then
            assertEquals(findCart, null);
        }
    }

    @Test
    @DisplayName("로그인한 사용자의 장바구니 목록 조회")
    public void cartList() {

        // given
        Cart cart = new Cart(product, user.getId(), 1);
        em.persist(cart);

        // when
        List<Cart> cartList = cartRepository.cartList(user.getId());

        // then
        assertEquals(cartList.size(), 1);
        assertEquals(cartList.get(0), cart);
    }

    @Nested
    @DisplayName("로그인한 사용자의 주문 가능한 장바구니 목록 조회")
    class orderList {

        @Test
        @DisplayName("주문 가능한 상품 있음")
        public void exist() {

            // given
            Cart cart = new Cart(product, user.getId(), 1);
            em.persist(cart);

            // when
            List<Cart> cartList = cartRepository.orderCartList(user.getId());

            // then
            assertEquals(cartList.size(), 1);
            assertEquals(cartList.get(0), cart);
        }

        @Test
        @DisplayName("주문 가능한 상품 없음")
        public void not_exist() {

            // given
            Cart cart = new Cart(product, user.getId(), 20);
            em.persist(cart);

            // when
            List<Cart> cartList = cartRepository.orderCartList(user.getId());

            // then
            assertEquals(cartList.size(), 0);
        }

    }

}