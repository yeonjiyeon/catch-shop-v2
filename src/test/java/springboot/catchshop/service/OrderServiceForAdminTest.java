package springboot.catchshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.InitDb;
import springboot.catchshop.admin.service.OrderServiceForAdmin;
import springboot.catchshop.domain.*;
import springboot.catchshop.dto.CartResponseDto;
import springboot.catchshop.dto.OrderDetailDto;
import springboot.catchshop.dto.OrderResponseDto;
import springboot.catchshop.dto.PaymentDto;
import springboot.catchshop.repository.OrderDetailRepository;
import springboot.catchshop.repository.OrderRepository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * OrderServiceForAdmin Test
 * author: soohyun, last modified: 22.04.23
 */

@SpringBootTest
@Transactional
class OrderServiceForAdminTest {

    @Autowired EntityManager em;
    @Autowired OrderServiceForAdmin orderServiceForAdmin;

    private Address address;
    private User user;
    private Product product;
    private Order order;
    private OrderDetail orderDetail;

    @BeforeEach
    public void berforeEach() {

        // 사용자 생성
        address = new Address("road1", "detail1", "11111");
        user = new User("user1", "user1", "user1", "01012345678", "user1@catchshop.ac.kr", address, "USER");
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
    }

    @Test
    @DisplayName("주문 전체 조회 + 페이징")
    public void getAllOrdersWithPaging() {

        // when
        Page<Order> findOrders = orderServiceForAdmin.getAllOrdersWithPaging(0);

        // then
        assertEquals(findOrders.getTotalPages(), 1);
        assertEquals(findOrders.getTotalElements(), 3); // InitDB에서 생성된 데이터 포함해서 4
        assertEquals(findOrders.getContent().size(), 3);
        assertThat(findOrders.getContent()).contains(order);
    }

    @Nested
    @DisplayName("주문 취소")
    class cancelOrder {

        @Test
        @DisplayName("성공")
        public void success() {

            // when
            orderServiceForAdmin.cancelOrder(order.getId());

            // then
            assertEquals(order.getOrderStatus(), OrderStatus.CANCEL);
        }

        @Test
        @DisplayName("실패")
        public void fail() {

            // given
            order.updateOrderStatus(OrderStatus.DELIVERY);

            // when
            orderServiceForAdmin.cancelOrder(order.getId());

            // then
            assertEquals(order.getOrderStatus(), OrderStatus.DELIVERY);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경")
    class updateOrderStatus {

        @Test
        @DisplayName("성공")
        public void success() {

            // when
            orderServiceForAdmin.updateOrderStatus(order.getId());

            // then
            assertEquals(order.getOrderStatus(), OrderStatus.DELIVERY);
        }

        @Test
        @DisplayName("실패")
        public void fail() {

            // given
            order.updateOrderStatus(OrderStatus.CANCEL);

            // when
            orderServiceForAdmin.updateOrderStatus(order.getId());

            // then
            assertEquals(order.getOrderStatus(), OrderStatus.CANCEL);
        }
    }
}