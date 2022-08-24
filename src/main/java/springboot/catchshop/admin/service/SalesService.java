package springboot.catchshop.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.admin.repository.SalesRepository;
import springboot.catchshop.domain.OrderDetail;

import java.util.List;

/**
 * 매출 service
 * author: 강수민, created: 22.04.15
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SalesService {

    private final SalesRepository salesRepository;

    // 기간별 매출 내역 조회
    @Transactional(readOnly = true)
    public List<OrderDetail> getSalesList(String searchStartDate, String searchEndDate) {
        return salesRepository.searchSales(searchStartDate, searchEndDate);
    }

    // 기간별 총 매출액 조회
    @Transactional(readOnly = true)
    public Long totalSalesPrice(String searchStartDate, String searchEndDate) {
        return salesRepository.totalSalesPrice(searchStartDate, searchEndDate);
    }
}
