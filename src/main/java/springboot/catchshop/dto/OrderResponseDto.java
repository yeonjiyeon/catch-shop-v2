package springboot.catchshop.dto;

import lombok.Data;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.Order;
import springboot.catchshop.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order Response Dto (조회시 주문 전체 정보를 담고 있는 Dto)
 * author: soohyun, last modified: 22.04.02
 */

@Data
public class OrderResponseDto {

    private Long id;
    private List<OrderDetailDto> orderDetailList; // 주문 상세 리스트
    private String orderName; // 수령자 이름
    private String orderTel; // 수령자 전화번호
    private String postcode;
    private String address;
    private OrderStatus orderStatus; // 주문 상태
    private LocalDateTime orderDate; // 주문 날짜
    private Long totalPrice; // 최종 상품 금액
    private Long shippingFee; // 배송비

    // 생성 메서드
    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.orderDetailList = order.getOrderDetailList().stream().map(d -> new OrderDetailDto(d)).collect(Collectors.toList());
        this.orderName = order.getOrderName();
        this.orderTel = order.getOrderTel();
        this.postcode = order.getPostcode();
        this.address = order.getAddress();
        this.orderStatus = order.getOrderStatus();
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.getTotalPrice();
        this.shippingFee = order.getShippingFee();
    }
}
