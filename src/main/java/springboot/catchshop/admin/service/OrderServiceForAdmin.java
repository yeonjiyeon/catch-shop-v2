package springboot.catchshop.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Order;
import springboot.catchshop.domain.OrderStatus;
import springboot.catchshop.domain.Product;
import springboot.catchshop.repository.OrderRepository;
import springboot.catchshop.service.PaymentService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
// 관리자용 Order Controller
// author: 수민, created: 22.03.26
public class OrderServiceForAdmin {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    // 주문 전체 조회 + 페이징
    @Transactional(readOnly = true)
    public Page<Order> getAllOrdersWithPaging(int startAt) {
        // 최신순 조회
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(startAt, 10, Sort.by(sorts));
        return orderRepository.findAll(pageable);
    }

    // 주문 취소
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalStateException("해당 주문이 존재하지 않습니다."));
        if (order.getOrderStatus() == OrderStatus.READY) {
            order.updateOrderStatus(OrderStatus.CANCEL);
            paymentService.requestCancel(order.getPayment_id());
        }
    }

    // 주문 상태 변경
    public void updateOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalStateException("해당 주문이 존재하지 않습니다."));
        // 준비 중이던 상품을 배송 완료로 변경
        if (order.getOrderStatus() == OrderStatus.READY) {
            order.updateOrderStatus(OrderStatus.DELIVERY);
        }
    }
}
