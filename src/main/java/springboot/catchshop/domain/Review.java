package springboot.catchshop.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
/**
 * Review 기능
 * author:김지연
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Review extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "review_id")//리뷰 번호
    private Long id;

    @Column(name = "review_contents")//리뷰 내용
    private String contents;


    @Column(name = "review_img")//리뷰 이미지
    private String img;

    @Column(name = "review_star")////리뷰 별점
    private int star;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")//상품
    private Product product;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")//유저
    private User user;

    public Review(User user, Product product, String contents, int star) {
        this.user = user;
        this.product = product;
        this.contents = contents;
        this.star = star;
    }


    public void changeUser(User user){
        this.user = user;
    }
    public void changeStar(int star){
        this.star = star;
    }

    public void changeContents(String contents){
        this.contents = contents;
    }

}
