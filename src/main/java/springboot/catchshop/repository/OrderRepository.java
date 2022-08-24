package springboot.catchshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.catchshop.admin.repository.SalesRepository;
import springboot.catchshop.domain.Order;
import springboot.catchshop.domain.User;

import java.util.List;

/**
 * Order Repository
 * author: soohyun, last modified: 22.03.08
 */
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
}
