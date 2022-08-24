package springboot.catchshop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String road; // 도로명 주소
    private String detail; // 상세 주소
    private String postalcode; // 우편 번호

    public Address(String road, String detail, String postalcode) {
        this.road = road;
        this.detail = detail;
        this.postalcode = postalcode;
    }
}
