package springboot.catchshop.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.Review;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.ReviewDTO;
import springboot.catchshop.service.ReviewService;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    EntityManager em;

    private Address address;
    private User user;
    private Product product;

    @BeforeEach
    public void beforeEach() {
        // 사용자 생성
        address = new Address("road1", "detail1", "11111");

        user = new User("user1", "user1", "user1", "01012345678", "a@naver.com",address, "사용자");

        em.persist(user);

        // 상품 생성
        product = new Product();
        product.changePrice(10000);
        product.changeStock(10);
        em.persist(product);
    }

    
    @DisplayName("날짜 테스트")
    @Test
    public void BaseTimeEntityTest(){
        //given
        LocalDateTime now = LocalDateTime.now();
        reviewRepository.save(new Review());

        //when
        List<Review> reviews = reviewRepository.findAll();

        //then
        Review review = reviews.get(0);
        System.out.println(">>>>>>>>registerDate = " + review.getRegDate());

        //assertEquals();
        assertNotEquals(review.getRegDate(), now);
    }

    
    @Test
    public void 리뷰_등록(){
        //given
        Review review = Review.builder().id(10000L).contents("contents1").star(5).build();

        //when
        Review saveReview = reviewRepository.save(review);
        Optional<Review> findReview = reviewRepository.findById(saveReview.getId());

        //then
        assertThat(saveReview.getId()).isEqualTo(findReview.get().getId());
    }

    
    @Test
    public void 리뷰_전체_조회(){
        //given
        Review review = Review.builder().id(10000L).contents("contents1").star(5).build();
        Review saveReview = reviewRepository.save(review);


        //when
        List<Review> allReview = reviewRepository.findAll();

        //then
        assertThat(allReview.size()).isEqualTo(4);

    }

    
    @Test
    public void 리뷰_수정(){
        //given
        Review review = Review.builder().id(10000L).contents("contents1").star(5).build();
        Review saveReview = reviewRepository.save(review);

        //when
        Optional<Review> findReview = reviewRepository.findById(saveReview.getId());
        findReview.get().changeContents("contents2");
        reviewRepository.save(review);

        //then
        assertThat(review.getContents()).isEqualTo("contents2");
    }

    
    @Test
    public void 리뷰_삭제(){
        //given
        Review review = Review.builder().id(10000L).contents("contents1").star(5).build();
        Review saveReview = reviewRepository.save(review);

        //when
        Optional<Review> findReview = reviewRepository.findById(saveReview.getId());
        reviewRepository.deleteById(findReview.get().getId());

        //then
        assertThat(findReview).isEqualTo(Optional.empty());


    }
    
    
    
    
}
