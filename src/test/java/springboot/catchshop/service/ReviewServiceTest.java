package springboot.catchshop.service;

import com.mysema.commons.lang.Assert;
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
import org.w3c.dom.stylesheets.LinkStyle;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.Review;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.ProductDTO;
import springboot.catchshop.dto.ReviewDTO;
import springboot.catchshop.repository.ReviewRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewServiceTest {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewService reviewService;
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


        // 상품 생성
        product = new Product();
        product.changePrice(10000);
        product.changeStock(10);
        em.persist(product);
    }


    
    @Test
    public void 리뷰_등록(){
        //given
        ReviewDTO reviewDTO = ReviewDTO.builder()
            .id(10000L).contents("contents1")
            .star(5)
            .build();

        //when
        Long registerID = reviewService.register(reviewDTO, user, product.getId());


        //then
        assertThat(registerID).isEqualTo(10000L);
    }

    
    @Test
    public void 리뷰_조회(){
        //given
        ReviewDTO reviewDTO = ReviewDTO.builder()
            .id(10000L).contents("contents1")
            .star(5)
            .build();
        Long registerID = reviewService.register(reviewDTO, user, product.getId());

        //when
        List<ReviewDTO> listProduct = reviewService.getListOfProduct(product.getId());

        //then
        assertThat(listProduct.size()).isEqualTo(1);
    }

    
    @Test
    public void 리뷰_수정(){
        //given
        Review review = Review.builder().id(10000L).contents("contents1").star(5).build();
        Review saveReview = reviewRepository.save(review);
        //Optional<Review> findReview = reviewRepository.findById(saveReview.getId());

        //when
        ReviewDTO reviewDTO = ReviewDTO.builder().id(saveReview.getId()).contents("contents2")
            .star(saveReview.getStar()).build();
        reviewService.modify(reviewDTO);
        Optional<Review> findReview2 = reviewRepository.findById(saveReview.getId());

        //then
        assertThat(findReview2.get().getContents()).isEqualTo("contents2");
    }

    
    @Test
    public void 리뷰_삭제(){
        //given
        Review review = Review.builder().id(10000L).contents("contents1").star(5).build();
        Review saveReview = reviewRepository.save(review);
        Optional<Review> findReview = reviewRepository.findById(saveReview.getId());

        //when
        reviewService.remove(findReview.get().getId());

        //then
        Optional<Review> findReview2 = reviewRepository.findById(findReview.get().getId());
        assertThat(findReview2).isEqualTo(Optional.empty());


    }
    
    
    
    
}
