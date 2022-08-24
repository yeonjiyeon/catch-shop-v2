package springboot.catchshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.catchshop.domain.Order;
import springboot.catchshop.domain.User;

import javax.validation.constraints.NotEmpty;

/**
 * Order Request Dto (주문시 주문 정보를 담고 있는 Dto)
 * author: soohyun, last modified: 22.04.02
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @NotEmpty(message = "이름을 입력하세요.")
    private String name; // 구매자 이름

    @NotEmpty(message = "전화번호를 입력하세요.")
    private String telephone; // 구매자 전화번호

    @NotEmpty(message = "우편 번호를 입력하세요.")
    private String postcode; // 구매자 우편번호

    @NotEmpty(message = "도로명 주소를 입력하세요.")
    private String address; // 구매자 주소

    public Order toEntity(User user, Long totalPrice, Long shippingFee) {
        return Order.builder()
                .user(user)
                .orderName(name)
                .orderTel(telephone)
                .postcode(postcode)
                .address(address)
                .totalPrice(totalPrice)
                .shippingFee(shippingFee)
                .build();
    }

}
