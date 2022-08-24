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
import springboot.catchshop.domain.*;
import springboot.catchshop.dto.CartResponseDto;
import springboot.catchshop.dto.OrderResponseDto;
import springboot.catchshop.service.CartService;
import springboot.catchshop.service.OrderService;
import springboot.catchshop.session.SessionConst;

import javax.persistence.EntityManager;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *  OrderController Test
 *  author: soohyun, last modified: 22.04.24
 */

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired EntityManager em;
    @Autowired CartService cartService;
    @Autowired OrderService orderService;
    @Autowired MockMvc mockMvc;
    private MockHttpSession session;

    private Address address;
    private User user;
    private Product product;
    private Cart cart;
    private Order order;
    private OrderDetail orderDetail;

    @BeforeEach
    public void beforeEach() {
        // 사용자 생성
        address = new Address("road1", "detail1", "11111");
        user = new User("admin1", "admin1", "admin1", "01012345678", "admin1@catchshop.ac.kr", address, "ADMIN");
        em.persist(user);

        // 상품 생성
        product = new Product();
        product.changePrice(10000);
        product.changeStock(10);
        em.persist(product);

        // 장바구니 생성
        cart = new Cart(product, user.getId(), 1);
        em.persist(cart);

        // 주문 생성
        order = new Order("imp_111111111111", user, "name1", "01012345678", "11111", "address1", 10000L, 3000L);
        em.persist(order);
        orderDetail = new OrderDetail(order, product, 1, (long) product.getPrice());
        em.persist(orderDetail);

        // 세션에 로그인한 사용자 저장
        session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_USER, user);
    }

    @Nested
    @DisplayName("주문 작성 페이지")
    class orderForm {

        @Test
        @DisplayName("로그인 X")
        public void login_no() throws Exception {

            // then
            mockMvc.perform(MockMvcRequestBuilders.get("/order"))
                    .andExpect(redirectedUrl("/login"));
        }

        @Test
        @DisplayName("로그인 O")
        public void login_ok() throws Exception {

            // given
            CartResponseDto carts = cartService.orderCartList(user.getId());

            // then
            mockMvc.perform(MockMvcRequestBuilders.get("/order")
                            .session(session))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("carts", carts))
                    .andExpect(view().name("orderForm"));

        }
    }

    @Nested
    @DisplayName("주문하기")
    class createOrder {

        @Test
        @DisplayName("주문 실패")
        public void fail() throws Exception {

            // then
            // 유효한 결제 번호가 아닌 경우
            mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                            .param("imp_uid", "imp_000000000000")
                            .param("paid_amount", Integer.toString(13000)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("fail"));

            // 결제 금액이 맞지 않는 경우
            mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                            .param("imp_uid", "imp_111111111111")
                            .param("paid_amount", Integer.toString(10000)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("fail"));
        }

        @Test
        @DisplayName("주문 성공")
        public void success() throws Exception {

            // then
            mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                            .param("imp_uid", "imp_111111111111")
                            .param("paid_amount", Integer.toString(13000)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("주문 내역 조회")
    class orderList {

        @Test
        @DisplayName("로그인 X")
        public void login_no() throws Exception {

            // then
            mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                    .andExpect(redirectedUrl("/login"));
        }

        @Test
        @DisplayName("로그인 O")
        public void login_ok() throws Exception {

            // given
            List<OrderResponseDto> orders = orderService.orderList(user);

            // then
            mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                            .session(session))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("orders", orders))
                    .andExpect(view().name("order"));
        }
    }

    @Test
    @DisplayName("주문 취소")
    public void cancelOrder() throws Exception {

        // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/" + order.getId()))
                .andExpect(redirectedUrl("/orders"));
    }
}
