package springboot.catchshop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *  OrderRepository Test
 *  author: soohyun, last modified: 22.04.07
 */

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired EntityManager em;
    @Autowired OrderRepository orderRepository;

    private Address address;
    private User user1, user2;
    private Product product;
    private Order order1, order2;
    private OrderDetail orderDetail1, orderDetail2;

    @BeforeEach
    public void beforeEach() {
        // 사용자 생성
        address = new Address("road1", "detail1", "11111");
        user1 = new User("user1", "user1", "user1", "01012345678", "user1@catchshop.ac.kr", address, "USER");
        em.persist(user1);
        user2 = new User("user2", "user2", "user2", "01012345678", "user2@catchshop.ac.kr", address, "USER");
        em.persist(user2);

        // 상품 생성
        product = new Product();
        product.changePrice(10000);
        product.changeStock(10);
        em.persist(product);

        // 주문 생성
        order1 = new Order("imp_111111111111", user1, "name1", "01012345678",
                "11111", "address1", 10000L, 3000L);
        em.persist(order1);
        orderDetail1 = new OrderDetail(order1, product, 1, 10000L);
        em.persist(orderDetail1);

        order2 = new Order("imp_222222222222", user2, "name2", "01012345678",
                "22222", "address2", 20000L, 3000L);
        em.persist(order2);
        orderDetail2 = new OrderDetail(order2, product, 2, 10000L);
        em.persist(orderDetail2);
    }


    @Test
    @DisplayName("로그인한 사용자로 주문 목록 조회")
    public void orderList() {

        // when
        List<Order> orderList = orderRepository.orderList(user1);

        // then
        assertEquals(orderList.size(), 1);
        assertEquals(orderList.get(0).getUser(), user1);
    }

    @Test
    @DisplayName("관리자용 주문 목록 조회")
    public void orderListForAdmin() {

        // when
        List<Order> orderList = orderRepository.orderListForAdmin();

        // then
        assertEquals(orderList.size(), 4);
        assertEquals(orderList.get(0).getUser(), user2);
        assertEquals(orderList.get(0).getTotalPrice(), 20000L);
        assertEquals(orderList.get(1).getUser(), user1);
        assertEquals(orderList.get(1).getTotalPrice(), 10000L);;
    }

    @Test
    @DisplayName("관리자용 주문 상세 조회")
    public void orderDetailListForAdmin() {

        // when
        List<OrderDetail> orderDetailList = orderRepository.orderDetailListForAdmin(order1.getId());

        // then
        assertEquals(orderDetailList.size(), 1);
    }


}