package springboot.catchshop.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.QuestionCategory;
import springboot.catchshop.domain.User;
import springboot.catchshop.domain.Question;
import springboot.catchshop.dto.QuestionDto;
import springboot.catchshop.repository.ProductRepository;
import springboot.catchshop.repository.QuestionRepository;
import springboot.catchshop.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

// author: 강수민
@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionRepository questionRepository;

    private User user;
    private Product product;

    @BeforeEach
    public void beforeEach() {
        // 사용자 생성
        Address address = new Address("road1", "detail1", "11111");
        user = new User("user1", "user1", "user1", "01012345678",
                "user1@catchshop.ac.kr", address, "USER");
        userRepository.save(user);

        // 상품 생성
        product = new Product();
        product.changePrice(10000);
        product.changeStock(10);
        productRepository.save(product);
    }

    @Test
    void 질문생성() throws Exception {
        // given
        QuestionDto dto = new QuestionDto(true, "1234", QuestionCategory.PRODUCT.toString(),
                "문의 드림니다~");

        // when
        Question question = questionService.saveQuestion(user, product.getId(), dto);

        // then
        System.out.println(question.getCategory());
    }

    @Test
    void findQuestionsByProduct() throws Exception {
        QuestionDto dto = new QuestionDto(true, "1234", QuestionCategory.PRODUCT.toString(),
                "문의 드림니다~");
        Question question = questionService.saveQuestion(user, product.getId(), dto);

        List<Question> questions = questionRepository.findByProductId(product.getId());

        Assertions.assertThat(questions.size()).isEqualTo(1);
    }
}
