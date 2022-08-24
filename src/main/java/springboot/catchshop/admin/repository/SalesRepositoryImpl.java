package springboot.catchshop.admin.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import springboot.catchshop.domain.OrderDetail;
import springboot.catchshop.domain.OrderStatus;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static springboot.catchshop.domain.QOrderDetail.orderDetail;
import static springboot.catchshop.domain.QOrder.order;
import static springboot.catchshop.domain.QProduct.product;


/**
 * 매출 respositoryImpl
 * author: 강수민, created: 22.04.15
 */
@Repository
public class SalesRepositoryImpl implements SalesRepository {

    private final JPAQueryFactory queryFactory;

    public SalesRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

//    // datetime format 변경
//    public StringTemplate convertDateFormat(DateTimePath<LocalDateTime> datetime, String format) {
//        return stringTemplate(
//                "FORMATDATETIME({0}, {1})"
//                , datetime
//                , ConstantImpl.create(format));
//    }

    // string -> LocalDateTime
    public LocalDateTime strToDate(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(strDate, formatter).atStartOfDay(); // 하루의 시작 시간
    }

    // 기간별 매출 내역 조회
    @Override
    public List<OrderDetail> searchSales(String searchStartDate, String searchEndDate) {
        LocalDateTime startDate = strToDate(searchStartDate);
        LocalDateTime endDate = strToDate(searchEndDate).with(LocalTime.MAX); // 하루의 마지막 시간

        return queryFactory
                .selectFrom(orderDetail)
                .join(orderDetail.order, order)
                .join(orderDetail.product, product)
                .where(orderDetail.order.orderStatus.ne(OrderStatus.CANCEL)
                        .and(orderDetail.regDate.between(startDate, endDate)))
                .fetch();
    }

    // 기간별 총 매출액 조회
    @Override
    public Long totalSalesPrice(String searchStartDate, String searchEndDate) {
        LocalDateTime startDate = strToDate(searchStartDate);
        LocalDateTime endDate = strToDate(searchEndDate).with(LocalTime.MAX); // 하루의 마지막 시간

        List<Long> totalPrices =
                queryFactory.select(orderDetail.totalPrice.sum())
                        .from(orderDetail)
                        .join(orderDetail.order, order)
                        .where(orderDetail.order.orderStatus.ne(OrderStatus.CANCEL)
                                .and(orderDetail.regDate.between(startDate, endDate)))
                        .fetch();
        return totalPrices.get(0);
    }

}
