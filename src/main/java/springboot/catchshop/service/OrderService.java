package springboot.catchshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.*;
import springboot.catchshop.dto.*;
import springboot.catchshop.repository.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Order Service
 * author: soohyun, last modified: 22.04.21
 */

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final PaymentService paymentService;

    // 주문 생성 (결제 시스템 포함)
    public Long createOrder(User loginUser, PaymentDto paymentDto) {

        CartResponseDto carts = cartService.orderCartList(loginUser.getId()); // 주문 가능한 장바구니 목록

        Order order = Order.builder()
                .payment_id(paymentDto.getImpUid())
                .user(loginUser)
                .orderName(paymentDto.getBuyerName())
                .orderTel(paymentDto.getBuyerTel())
                .postcode(paymentDto.getBuyerPostcode())
                .address(paymentDto.getBuyerAddr())
                .totalPrice(carts.getTotalAllProductPrice())
                .shippingFee(carts.getShippingFee())
                .build();

        Order saveOrder = orderRepository.save(order); // 주문 생성

        // 주문 상세 생성
        for(CartInfoDto cart: carts.getCartList()) {
            Product product = cart.getProduct();
            OrderDetail orderDetail = new OrderDetail(saveOrder, product, cart.getCartCount(), (long) product.getPrice());
            orderDetail.calTotalPrice(orderDetail.getOrderCount() * orderDetail.getOrderPrice());
            orderDetailRepository.save(orderDetail);
            product.removeStock(cart.getCartCount()); // 해당 상품 재고량 감소
            cartService.deleteCart(cart.getId()); // 장바구니에서 해당 상품 삭제
        }
        return order.getId();
    }

    // 주문 생성 (결제 시스템 제외)
//    public Long createOrder2(Order order, List<CartInfoDto> carts) {
//        Order saveOrder = orderRepository.save(order); // 주문 생성
//
//        // 주문 상세 생성
//        for(CartInfoDto cart: carts) {
//            Product product = cart.getProduct();
//            OrderDetail orderDetail = new OrderDetail(saveOrder, product, cart.getCartCount(), (long) product.getPrice());
//            orderDetailRepository.save(orderDetail);
//            product.removeStock(cart.getCartCount()); // 해당 상품 재고량 감소
//            cartService.deleteCart(cart.getId()); // 장바구니에서 해당 상품 삭제
//
//        }
//        return order.getId();
//    }


    // 주문 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto> orderList(User loginUser) {
        List<Order> orders = orderRepository.orderList(loginUser); // 로그인한 사용자로 주문 조회
        List<OrderResponseDto> orderList = orders.stream().map(o -> new OrderResponseDto(o)).collect(Collectors.toList());

        return orderList;
    }

    // 주문 취소
    public Long cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        // 주문 상태가 '준비중'일 경우 '취소'로 변경
        if (order.getOrderStatus() == OrderStatus.READY) {
            order.updateOrderStatus(OrderStatus.CANCEL);
            paymentService.requestCancel(order.getPayment_id());
        }

        return order.getId();
    }
}
