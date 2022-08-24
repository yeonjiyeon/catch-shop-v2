package springboot.catchshop.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.admin.service.OrderServiceForAdmin;
import springboot.catchshop.domain.Order;
import springboot.catchshop.domain.OrderDetail;
import springboot.catchshop.domain.User;
import springboot.catchshop.repository.OrderRepository;
import springboot.catchshop.session.SessionConst;

import java.util.List;

// 관리자용 Order Controller
// author: 강수민, created: 22.03.20
// last modified: 22.03.20
// author: soohyun, last modified: 22.04.24

@Controller
@RequiredArgsConstructor
public class OrderControllerForAdmin {

    private final OrderRepository orderRepository;
    private final OrderServiceForAdmin orderServiceForAdmin;

    // 전체 주문 조회
    @GetMapping("/orders/admin")
    public String orderListForAdmin(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                 Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        if (loginUser == null) {
            return "redirect:/login";
        }
        Page<Order> orderList = orderServiceForAdmin.getAllOrdersWithPaging(page);
        model.addAttribute("paging", orderList);

        return "admin/orders";
    }

    // 주문 상세
    @GetMapping("/orders/{id}/admin")
    public String orderDetailsForAdmin(Model model, @PathVariable("id") Long orderId) {
        List<OrderDetail> orderDetails = orderRepository.orderDetailListForAdmin(orderId);
        model.addAttribute("orderDetails", orderDetails);
        return "admin/orderDetails";
    }

     // 주문 취소
     @PatchMapping("/orders/{id}/admin")
     public String cancelOrderForAdmin(@PathVariable("id") Long orderId) {
         orderServiceForAdmin.cancelOrder(orderId);
         return "redirect:/orders/admin";
     }

     // 주문 상태 변경
     @PatchMapping("/orders/{id}/admin/status")
     public String updateOrderStatusForAdmin(@PathVariable("id") Long orderId) {
         orderServiceForAdmin.updateOrderStatus(orderId);
         return "redirect:/orders/admin";
     }
}
