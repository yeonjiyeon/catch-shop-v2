package springboot.catchshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Cart;
import springboot.catchshop.domain.Product;
import springboot.catchshop.dto.CartRequestDto;
import springboot.catchshop.dto.CartInfoDto;
import springboot.catchshop.dto.CartResponseDto;
import springboot.catchshop.repository.CartRepository;
import springboot.catchshop.repository.ProductRepository;
import springboot.catchshop.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Cart Service
 * author: soohyun, last modified: 22.03.05
 */

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // 장바구니 생성
    public Long addCart(Long userId, Long productId, int count) {
        Product product = productRepository.findById(productId).orElseThrow( () -> new IllegalStateException("상품이 존재하지 않습니다."));
        Cart findCart = cartRepository.findByUserIdAndProduct(userId, product); // 로그인한 사용자 id로 해당 상품이 담긴 장바구니 조회

        if (findCart == null) { // 해당 상품이 담긴 장바구니가 없는 경우
            CartRequestDto cartDto = new CartRequestDto(userId, product);
            Cart saveCart = cartRepository.save(cartDto.toEntity()); // 새로운 장바구니 생성
            saveCart.updateCartCount(count);
            return saveCart.getId();
        } else { // 해당 상품이 담긴 장바구니가 있는 경우
            findCart.updateCartCount(findCart.getCartCount() + count); // 기존 장바구니 수량 변경
            return findCart.getId();
        }
    }

    // 장바구니 전체 목록 조회
    @Transactional(readOnly = true)
    public CartResponseDto cartList(Long userId) {
        List<Cart> carts = cartRepository.cartList(userId); // 장바구니 목록 조회
        List<CartInfoDto> cartList = carts.stream().map(c -> new CartInfoDto(c)).collect(Collectors.toList());
        CartResponseDto cartResponseDto = new CartResponseDto(cartList); // 장바구니 관련 정보 조회

        return cartResponseDto;
    }

    // 주문 가능한 장바구니 목록 조회
    @Transactional(readOnly = true)
    public CartResponseDto orderCartList(Long userId) {
        List<Cart> carts = cartRepository.orderCartList(userId); // 주문 가능한 장바구니 목록 조회
        List<CartInfoDto> cartList = carts.stream().map(c -> new CartInfoDto(c)).collect(Collectors.toList());
        CartResponseDto cartResponseDto = new CartResponseDto(cartList); // 장바구니 관련 정보 조회

        return cartResponseDto;
    }

    // 장바구니 수정
    public Long updateCart(Long id, int cartCount) {
        Cart cart = cartRepository.findById(id).orElseThrow( () -> new IllegalStateException("장바구니가 존재하지 않습니다."));
        cart.updateCartCount(cartCount); // 상품 수량 변경

        return cart.getId();
    }

    // 장바구니 삭제
    public Long deleteCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow( () -> new IllegalStateException("장바구니가 존재하지 않습니다."));
        cartRepository.delete(cart);

        return cart.getId();
    }
}
