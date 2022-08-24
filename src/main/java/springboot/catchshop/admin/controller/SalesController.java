package springboot.catchshop.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.admin.service.SalesService;
import springboot.catchshop.domain.OrderDetail;
import springboot.catchshop.domain.User;
import springboot.catchshop.session.SessionConst;

import java.util.List;

/**
 * 매출 controller
 * author: 강수민, created: 22.04.15
 */
@Controller
@RequiredArgsConstructor
@Log4j2
public class SalesController {

    private final SalesService salesService;

    // 기간별 매출 내역 조회
    @RequestMapping(value = "/sales", method = { RequestMethod.GET })
    public String getSalesList(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model,
                               @RequestParam(value = "searchStartDate", required = false, defaultValue = "2022-04-19") String searchStartDate,
                               @RequestParam(value = "searchEndDate", required = false, defaultValue = "2022-04-19") String searchEndDate) {
        if (loginUser != null) { // 로그인 사용자의 경우
            // 매출 상세 내역
            List<OrderDetail> salesList = salesService.getSalesList(searchStartDate, searchEndDate);
            log.info("list : " + salesList);
            model.addAttribute("salesList", salesList);

            // 총 매출액
            Long totalSalesPrice = salesService.totalSalesPrice(searchStartDate, searchEndDate);
            model.addAttribute("totalSalesPrice", totalSalesPrice);

            return "admin/sales";
        } else { // 로그인하지 않은 사용자의 경우
            return "redirect:/login";
        }

    }

}
