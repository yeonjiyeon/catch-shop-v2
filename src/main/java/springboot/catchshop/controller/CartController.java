package springboot.catchshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.*;
import springboot.catchshop.service.CartService;
import springboot.catchshop.session.SessionConst;

/**
 * Cart Controller
 * author: soohyun, last modified: 22.02.25
 */

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 추가
    @PostMapping("/carts/{id}")
    public String addCart(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                          @PathVariable("id") Long productId, @RequestParam(value="count", defaultValue="1") int count) {

        if (loginUser != null) { // 로그인한 경우 장바구니 추가
            Long cartId = cartService.addCart(loginUser.getId(), productId, count);
        }

        return "redirect:/carts";
    }

    // 장바구니 조회
    @GetMapping("/carts")
    public String cartList(Model model, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {

        if (loginUser != null) { // 로그인한 사용자라면 장바구니 화면으로
            CartResponseDto carts = cartService.cartList(loginUser.getId());
            model.addAttribute("carts", carts);
            return "cart";
        } else { // 로그인하지 않은 사용자라면 로그인 화면으로
            return "redirect:/login";
        }
    }

    // 장바구니 수정
    @PatchMapping("/carts/{id}")
    public String updateCart(@PathVariable("id") Long id, @RequestParam("count") int count) {

        cartService.updateCart(id, count);
        return "redirect:/carts";
    }

    // 장바구니 삭제
    @DeleteMapping("/carts/{id}")
    public String deleteCart(@PathVariable("id") Long id) {

        cartService.deleteCart(id);
        return "redirect:/carts";
    }
}
