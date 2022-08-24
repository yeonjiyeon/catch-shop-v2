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
import springboot.catchshop.session.SessionConst;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *  OrderControllerForAdmin Test
 *  author: soohyun, last modified: 22.04.24
 */

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class OrderControllerForAdminTest {

    @Autowired EntityManager em;
    @Autowired MockMvc mockMvc;
    private MockHttpSession session;

    private Address address;
    private User user;
    private Product product;
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
    @DisplayName("전체 주문 조회")
    class orderListForAdmin {

        @Test
        @DisplayName("로그인 X")
        public void login_no() throws Exception {

            // then
            mockMvc.perform(MockMvcRequestBuilders.get("/orders/admin"))
                    .andExpect(redirectedUrl("/login"));
        }

        @Test
        @DisplayName("로그인 O")
        public void login_ok() throws Exception {

            // then
            mockMvc.perform(MockMvcRequestBuilders.get("/orders/admin")
                            .session(session)
                            .param("page", Integer.toString(0)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/orders"));
        }
    }

    @Test
    @DisplayName("주문 상세")
    public void orderDetailsForAdmin() throws Exception {

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + order.getId() + "/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/orderDetails"));
    }

    @Test
    @DisplayName("주문 취소")
    public void cancelOrderForAdmin() throws Exception {

        // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/" + order.getId() + "/admin"))
                .andExpect(redirectedUrl("/orders/admin"));
    }

    @Test
    @DisplayName("주문 상태 변경")
    public void updateOrderStatusForAdmin() throws Exception {

        // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/" + order.getId() + "/admin/status"))
                .andExpect(redirectedUrl("/orders/admin"));
    }
}