package springboot.catchshop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Entity
 * author: soohyun, last modified: 22.04.09
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    private String payment_id; // 결제 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 주문한 사용자

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList = new ArrayList<>(); // 주문 상세 리스트

    private String orderName; // 수령자 이름
    private String orderTel; // 수령자 전화번호
    private String postcode; // 우편번호
    private String address; // 수령 주소

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    @CreatedDate
    private LocalDateTime orderDate; // 주문 날짜

    private Long totalPrice; // 최종 상품 금액
    private Long shippingFee; // 배송비

    // 생성 메서드
    @Builder
    public Order(String payment_id, User user, String orderName, String orderTel, String postcode, String address, Long totalPrice, Long shippingFee) {
        this.payment_id = payment_id;
        this.user = user;
        this.orderName = orderName;
        this.orderTel = orderTel;
        this.postcode = postcode;
        this.address = address;
        this.orderStatus = OrderStatus.READY;
        this.totalPrice = totalPrice;
        this.shippingFee = shippingFee;
    }

    // 주문 상태 변경
    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
