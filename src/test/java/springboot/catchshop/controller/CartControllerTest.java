package springboot.catchshop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.Cart;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.CartResponseDto;
import springboot.catchshop.service.CartService;
import springboot.catchshop.session.SessionConst;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *  CartController Test
 *  author: soohyun, last modified: 22.04.21
 */

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired EntityManager em;
    @Autowired CartService cartService;
    @Autowired MockMvc mockMvc;
    private MockHttpSession session;

    private Address address;
    private User user;
    private Product product;
    private Cart cart;

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

        // 장바구니 생성
        cart = new Cart(product, user.getId(), 1);
        em.persist(cart);

        // 세션에 로그인한 사용자 저장
        session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_USER, user);
    }

    @Test
    @DisplayName("장바구니 추가")
    public void addCart() throws Exception {

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/carts/" + product.getId())
                        .session(session)
                        .param("count", Integer.toString(1)))
                .andExpect(redirectedUrl("/carts"));
    }

    @Nested
    @DisplayName("장바구니 조회")
    class cartList {

        @Test
        @DisplayName("로그인 X")
        public void login_no() throws Exception{

            // then
            mockMvc.perform(MockMvcRequestBuilders.get("/carts"))
                    .andExpect(redirectedUrl("/login"));
        }

        @Test
        @DisplayName("로그인 O")
        public void login_ok() throws Exception {

            // given
            CartResponseDto carts = cartService.cartList(user.getId()); // 장바구니 목록

            // then
            mockMvc.perform(MockMvcRequestBuilders.get("/carts")
                            .session(session))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("carts", carts));
        }
    }

    @Test
    @DisplayName("장바구니 수정")
    public void updateCart() throws Exception {

        // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/carts/" + cart.getId())
                        .param("count", Integer.toString(2)))
                .andExpect(redirectedUrl("/carts"));
    }

    @Test
    @DisplayName("장바구니 삭제")
    public void deleteCart() throws Exception {

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/" + cart.getId()))
                .andExpect(redirectedUrl("/carts"));
    }
}