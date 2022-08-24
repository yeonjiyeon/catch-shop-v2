package springboot.catchshop.dto;

import com.siot.IamportRestClient.response.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Payment Dto (결제 정보를 담고 있는 Dto)
 * author: soohyun, last modified: 22.04.21
 */

@Data
@AllArgsConstructor
public class PaymentDto {

    private String impUid; // 결제 번호
    private String buyerName; // 구매자 이름
    private String buyerTel; // 구매자 번호
    private String buyerPostcode; // 구매자 우편번호
    private String buyerAddr; // 구매자 주소

    // 생성 메서드
    public PaymentDto(Payment payment) {
        this.impUid = payment.getImpUid();
        this.buyerName = payment.getBuyerName();
        this.buyerTel = payment.getBuyerTel();
        this.buyerPostcode = payment.getBuyerPostcode();
        this.buyerAddr = payment.getBuyerAddr();
    }
}
