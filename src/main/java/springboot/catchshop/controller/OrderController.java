package springboot.catchshop.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.domain.Order;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.*;
import springboot.catchshop.service.CartService;
import springboot.catchshop.service.OrderService;
import springboot.catchshop.service.PaymentService;
import springboot.catchshop.session.SessionConst;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Order Controller
 * author: soohyun, last modified: 22.04.09
 */

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    // 주문 작성 페이지
    @GetMapping("/order")
    public String orderForm(Model model, @ModelAttribute("orderForm") OrderRequestDto form,
                            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {

        if (loginUser != null) { // 로그인한 사용자라면 주문 작성 페이지로
            CartResponseDto carts = cartService.orderCartList(loginUser.getId());
            model.addAttribute("carts", carts);
            return "orderForm";
        } else { // 로그인하지 않은 사용자라면 로그인 화면으로
            return "redirect:/login";
        }
    }

    // 주문하기
    @PostMapping("/orders")
    @ResponseBody
    public String createOrder(@RequestParam("imp_uid") String imp_uid, @RequestParam("paid_amount") BigDecimal paid_amount,
                                            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {

        IamportResponse<Payment> response = paymentService.requestPayment(imp_uid);

        if (response != null && paid_amount.equals(response.getResponse().getAmount())) {
            PaymentDto paymentDto = new PaymentDto(response.getResponse());
            orderService.createOrder(loginUser, paymentDto);
            return "success";
        } else {
            paymentService.requestCancel(imp_uid);
            return "fail";
        }
    }

    // 주문 내역 조회
    @GetMapping("/orders")
    public String orderList(Model model, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {
        if (loginUser != null) { // 로그인한 사용자라면 주문 내역 화면으로
            List<OrderResponseDto> orders = orderService.orderList(loginUser);
            model.addAttribute("orders", orders);
            return "order";
        } else { // 로그인하지 않은 사용자라면 로그인 화면으로
            return "redirect:/login";
        }
    }

    // 주문 취소
    @PatchMapping("/orders/{id}")
    public String cancelOrder(@PathVariable("id") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}