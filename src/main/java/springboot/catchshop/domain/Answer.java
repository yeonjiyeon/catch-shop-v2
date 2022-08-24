package springboot.catchshop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

    @GeneratedValue
    @Id
    @Column(name = "answer_id")
    private Long id;

    // fk
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // fk
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") // 답변 작성자 번호
    private User user;

    private String content;

    public Answer(User user, Question question, String content) {
        this.user = user;
        this.question = question;
        this.content = content;
        user.getAnswerList().add(this);
        question.getAnswerList().add(this);
    }

    public void updateAnswer(String contents) {
        this.content = contents;
    }

}
