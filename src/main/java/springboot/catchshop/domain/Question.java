package springboot.catchshop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @GeneratedValue
    @Id
    @Column(name = "question_id")
    private Long id;

    // fk
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") // 작성자 번호
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id") // 상품 번호
    private Product product;

    private String category;
    private String content;

    private String imgName; // 이미지명
    private String imgPath; // 이미지 경로

    private String secret; // 비밀글 여부

    private String password;

    private String answered; // 답변 여부

    @OneToMany(mappedBy = "question")
    private final List<Answer> answerList = new ArrayList<>();

    public void updateImageInfo(String imgName) {
        this.imgName = imgName;
        this.imgPath = "/files/" + imgName;
    }

    public Question(User user, Product product, String category,
                    String content, Boolean secret, String password) {
        this.user = user;
        this.product = product;
        this.category =category;
        this.content = content;
        if (secret) {
            this.secret = "secret";
            this.password = password;
        } else {
            this.secret = "open";
        }
        this.answered = "미답변";
        user.getQuestionList().add(this);
        product.getQuestionList().add(this);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateAnswered() {
        this.answered = "답변 완료";
    }

}
