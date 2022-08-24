package springboot.catchshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.admin.repository.SalesRepository;
import springboot.catchshop.admin.service.SalesService;
import springboot.catchshop.domain.*;
import springboot.catchshop.repository.OrderRepository;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SalesServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    SalesRepository salesRepository;
    @Autowired
    SalesService salesService;
    @Autowired
    OrderRepository orderRepository;

    private Address address;
    private User user1;
    private Product product;
    private Order order1;
    private OrderDetail orderDetail1;

    @BeforeEach
    public void beforeEach() {
        // 사용자 생성
        address = new Address("road1", "detail1", "11111");
        user1 = new User("user1", "user1", "user1", "01012345678", "user1@catchshop.ac.kr", address, Role.USER.toString());
        em.persist(user1);

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
        orderDetail1.calTotalPrice(orderDetail1.getOrderCount() * orderDetail1.getOrderPrice());
        em.persist(orderDetail1);

    }

    @DisplayName("매출 내역 조회")
    @Test
    public void searchSales() {
        LocalDate date = orderDetail1.getRegDate().toLocalDate();
        List<OrderDetail> salesList = salesService.getSalesList(date.toString(), date.toString());

        for (OrderDetail orderDetail : salesList) {
            assertEquals(orderDetail.getRegDate().toLocalDate(), date);
            assertNotSame(orderDetail.getOrder().getOrderStatus(), OrderStatus.CANCEL); // 취소되지 않은 주문만 조회
        }
    }

    @DisplayName("총 매출액 조회")
    @Test
    public void totalSalesPrice() {
        String date = orderDetail1.getRegDate().toLocalDate().toString();
        Long totalSalesPrice = salesService.totalSalesPrice(date, date);
        List<OrderDetail> salesList = salesService.getSalesList(date, date);

        int sum = 0;
        for (OrderDetail orderDetail : salesList) {
            sum += orderDetail.getTotalPrice();
        }

        assertEquals(sum, totalSalesPrice);
    }

    @DisplayName("주문 취소 후 매출액 테스트")
    @Test
    public void totalSalesPriceAfterCancelOrder() {
        String date = orderDetail1.getRegDate().toLocalDate().toString();
        // 주문 취소 전 매출액
        Long totalSalesPriceBeforeCancelOrder = salesService.totalSalesPrice(date, date);
        order1.updateOrderStatus(OrderStatus.CANCEL);
        // 주문 취소 후 매출액
        Long totalSalesPriceAfterCancelOrder = salesService.totalSalesPrice(date, date);

        assertEquals(totalSalesPriceAfterCancelOrder, totalSalesPriceBeforeCancelOrder - order1.getTotalPrice());
    }
}
