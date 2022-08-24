package springboot.catchshop.dto;
/**
 * Product DTO
 * author:김지연
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.catchshop.domain.Category;

import java.time.LocalDateTime;
import springboot.catchshop.domain.ProductStatus;
import springboot.catchshop.domain.Review;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {
    private Long id;

    private String name;
    private String text;
    private int price;
    private int stock;
    private String productImg;
    private LocalDateTime regDate, modDate;
    private Category category;
    private ProductStatus productStatus;
    //private Review review;

    //상품의 평균 별점
    private double avg;

    //리뷰 개수
    private Long reviewCnt;
}
