package springboot.catchshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import springboot.catchshop.domain.*;

import javax.persistence.EntityManager;
import java.util.List;

import static springboot.catchshop.domain.QOrder.order;
import static springboot.catchshop.domain.QOrderDetail.orderDetail;
import static springboot.catchshop.domain.QProduct.product;
import static springboot.catchshop.domain.QUser.user;

/**
 * Order Repository Impl
 * author: soohyun, last modified: 22.03.11
 */
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 로그인한 사용자로 주문 목록 조회 (최신순)
    @Override
    public List<Order> orderList(User user) {
        return queryFactory
                .selectFrom(order).distinct()
                .join(order.orderDetailList, orderDetail).fetchJoin()
                .join(orderDetail.product, product).fetchJoin()
                .where(order.user.eq(user))
                .orderBy(order.orderDate.desc())
                .fetch();
    }

    // 관리자용 주문 목록 조회 (최신순)
    @Override
    public List<Order> orderListForAdmin() {
        return queryFactory
                .selectFrom(order)
                .join(order.user, user).fetchJoin()
                .orderBy(order.orderDate.desc())
                .fetch();
    }

    // 관리자용 주문 상세 조회
    @Override
    public List<OrderDetail> orderDetailListForAdmin(Long orderId) {
        return queryFactory
                .selectFrom(orderDetail)
                .join(orderDetail.order, order).fetchJoin()
                .join(orderDetail.product, product).fetchJoin()
                .where(orderDetail.order.id.eq(orderId))
                .fetch();
    }
}
