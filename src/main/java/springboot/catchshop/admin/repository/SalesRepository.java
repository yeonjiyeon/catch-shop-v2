package springboot.catchshop.admin.repository;

import org.springframework.stereotype.Repository;
import springboot.catchshop.domain.OrderDetail;

import java.util.List;

/**
 * 매출 repository
 * author: 강수민, created: 22.04.15
 */
@Repository
public interface SalesRepository {

    // 기간별 매출 내역 조회
    List<OrderDetail> searchSales(String searchStartDate, String searchEndDate);

    // 기간별 매출 총액 조회
    Long totalSalesPrice(String searchStartDate, String searchEndDate);

}
