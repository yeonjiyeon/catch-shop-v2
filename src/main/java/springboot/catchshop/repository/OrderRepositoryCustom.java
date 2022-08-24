package springboot.catchshop.repository;

import springboot.catchshop.domain.Order;
import springboot.catchshop.domain.OrderDetail;
import springboot.catchshop.domain.User;

import java.util.List;

/**
 * Order Repository Custom
 * author: soohyun, last modified: 22.03.26
 */
public interface OrderRepositoryCustom {

    // 로그인한 사용자로 주문 목록 조회
    List<Order> orderList(User user);

    // 관리자용 주문 목록 조회
    List<Order> orderListForAdmin();

    // 관리자용 주문 상세 조회
    List<OrderDetail> orderDetailListForAdmin(Long orderId);
}
