package springboot.catchshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.Review;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.ReviewDTO;
import springboot.catchshop.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;



    //모든 상품리뷰 가져오기
    @Override
    public List<ReviewDTO> getListOfProduct(Long pId) {
        Product product = Product.builder().id(pId).build();
        List<Review> result = reviewRepository.findByProduct(product);

        return result.stream().map(productReview -> entityToDTO(productReview)).collect(Collectors.toList());
    }


    //상품리뷰 추가
    @Override
    public Long register(ReviewDTO productReviewDTO, User user, Long productId) {
        productReviewDTO.setPId(productId);
        Review productReview = dtoToEntity(productReviewDTO);
        productReview.changeUser(user);
        reviewRepository.save(productReview);
        return productReview.getId();
    }

    //상품리뷰 수정
    @Override
    public void modify(ReviewDTO productReviewDTO) {
        Optional<Review> result = reviewRepository.findById(productReviewDTO.getId());
        if (result.isPresent()){
            Review productReview = result.get();
            productReview.changeStar(productReviewDTO.getStar());
            productReview.changeContents(productReviewDTO.getContents());

            reviewRepository.save(productReview);
        }
    }

    //상품리뷰 삭제
    @Override
    public void remove(Long id) {
        reviewRepository.deleteById(id);
    }


}
