package springboot.catchshop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.catchshop.exception.NotEnoughStockException;

import javax.persistence.*;

/**
 * Cart Entity
 * author: soohyun, last modified: 22.03.05
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 상품

    private Long userId; // 로그인한 사용자 번호
    private int cartCount; // 상품 수량

    // 생성 메서드
    @Builder
    public Cart(Product product, Long userId) {
        this.product = product;
        this.userId = userId;
    }

    @Builder
    public Cart(Product product, Long userId, int cartCount) {
        this.product = product;
        this.userId = userId;
        this.cartCount = cartCount;
    }

    // 수량 변경 메서드
    public void updateCartCount(int count) {
        if(count < 1) {
            throw new NotEnoughStockException("최소 수량은 1개입니다.");
        } else if (count > product.getStock() ) {
            throw new NotEnoughStockException("재고수량을 초과하였습니다.");
        }
        this.cartCount = count;
    }
}
