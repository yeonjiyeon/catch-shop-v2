package springboot.catchshop.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.*;
import springboot.catchshop.dto.CartResponseDto;
import springboot.catchshop.exception.NotEnoughStockException;
import springboot.catchshop.repository.CartRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  CartService Test
 *  author: soohyun, last modified: 22.03.07
 */

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired EntityManager em;
    @Autowired CartRepository cartRepository;
    @Autowired CartService cartService;

    private Address address;
    private User user;
    private Product product;
    private int count = 1; // 상품 수량

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
    @DisplayName("장바구니 생성")
    class addCart {

        @Test
        @DisplayName("성공_장바구니에 해당 상품이 없는 경우")
        public void success1() {

            // when
            Long addId = cartService.addCart(user.getId(), product.getId(), count);

            // then
            Optional<Cart> findCart = cartRepository.findById(addId);

            assertEquals(findCart.get().getId(), addId);
            assertEquals(findCart.get().getUserId(), user.getId());
            assertEquals(findCart.get().getProduct(), product);
            assertEquals(findCart.get().getCartCount(), 1);
        }

        @Test
        @DisplayName("성공_장바구니에 해당 상품이 있는 경우")
        public void success2() {

            // given
            Long cartId = cartService.addCart(user.getId(), product.getId(), count);

            // when
            Long addId = cartService.addCart(user.getId(), product.getId(), count);

            // then
            Optional<Cart> findCart = cartRepository.findById(addId);

            assertEquals(findCart.get().getId(), addId);
            assertEquals(findCart.get().getUserId(), user.getId());
            assertEquals(findCart.get().getProduct(), product);
            assertEquals(findCart.get().getCartCount(), 2);
        }

        @Test
        @DisplayName("실패")
        public void fail() throws NotEnoughStockException {

            // when
            NotEnoughStockException thrown = assertThrows(
                    NotEnoughStockException.class, () -> cartService.addCart(user.getId(), product.getId(), 20));

            // then
            assertEquals("재고수량을 초과하였습니다.", thrown.getMessage());
        }
    }

    @Test
    @DisplayName("장바구니 조회")
    public void cartList() throws Exception {

        // given
        cartService.addCart(user.getId(), product.getId(), count);

        // when
        CartResponseDto carts = cartService.cartList(user.getId());

        // then
        assertEquals(carts.getCartList().size(), 1);
        assertEquals(carts.getCartList().get(0).getProduct(), product);
        assertEquals(carts.getCartList().get(0).getProduct().getPrice(), 10000);
        assertEquals(carts.getCartList().get(0).getCartCount(), 1);
        assertEquals(carts.getCartList().get(0).getTotalProductPrice(), 10000);
        assertEquals(carts.getCartList().get(0).getUnderStock(), true);
        assertEquals(carts.getTotalAllProductPrice(), 10000);
        assertEquals(carts.getShippingFee(), 3000);
        assertEquals(carts.getTotalPayPrice(), 13000);
    }

    @Nested
    @DisplayName("장바구니 수정")
    class updateCart {

        @Test
        @DisplayName("성공")
        public void success() {

            // given
            Long cartId = cartService.addCart(user.getId(), product.getId(), count);

            // when
            Long updateId = cartService.updateCart(cartId, 2);

            // then
            Optional<Cart> findCart = cartRepository.findById(updateId);

            assertEquals(findCart.get().getId(), updateId);
            assertEquals(findCart.get().getUserId(), user.getId());
            assertEquals(findCart.get().getProduct(), product);
            assertEquals(findCart.get().getCartCount(), 2);
        }

        @Test
        @DisplayName("실패")
        public void fail() throws NotEnoughStockException {

            // given
            Long cartId = cartService.addCart(user.getId(), product.getId(), count);

            // when
            NotEnoughStockException thrown = assertThrows(
                    NotEnoughStockException.class, () -> cartService.updateCart(cartId, 20));

            // then
            assertEquals("재고수량을 초과하였습니다.", thrown.getMessage());
        }
    }

    @Test
    @DisplayName("장바구니 삭제")
    public void deleteCart() {

        // given
        Long cartId = cartService.addCart(user.getId(), product.getId(), count);

        // when
        Long deleteId = cartService.deleteCart(cartId);

        // then
        Optional<Cart> findCart = cartRepository.findById(deleteId);

        assertEquals(findCart, Optional.empty());
    }
}