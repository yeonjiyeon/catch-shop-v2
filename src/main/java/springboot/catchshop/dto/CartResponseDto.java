package springboot.catchshop.dto;

import lombok.Data;

import java.util.List;

/**
 * Cart Response Dto (조회시 장바구니 전체 정보를 담고 있는 Dto)
 * author: soohyun, last modified: 22.03.07
 */

@Data
public class CartResponseDto {

    private List<CartInfoDto> cartList; // 장바구니 목록
    private Long totalAllProductPrice; // 전체 상품 가격
    private Long shippingFee; // 배송비
    private Long totalPayPrice; // 최종 결제 금액

    // 생성 메서드
    public CartResponseDto(List<CartInfoDto> cartList) {
        this.cartList = cartList;
        this.totalAllProductPrice = calTotalAllProductPrice();
        this.shippingFee = calShippingFee();
        this.totalPayPrice = totalAllProductPrice + shippingFee;
    }

    // 계산 메서드
    private Long calTotalAllProductPrice() {
        Long price = 0L;

        for (CartInfoDto cartListDto : cartList) {
            if (cartListDto.getUnderStock()) {
                price += cartListDto.getTotalProductPrice();
            }
        }

        return  price;
    }

    private Long calShippingFee() {
        if (totalAllProductPrice >= 50000) {
            return 0L;
        } else {
            return 3000L;
        }
    }

}
