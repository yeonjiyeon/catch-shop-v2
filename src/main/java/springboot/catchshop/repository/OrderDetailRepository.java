package springboot.catchshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.catchshop.domain.OrderDetail;

import java.util.List;

/**
 * Order Detail Repository
 * author: soohyun, last modified: 22.03.05
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    // 주문 번호로 주문 상세 조회
    List<OrderDetail> findByOrderId(Long orderId);
}
