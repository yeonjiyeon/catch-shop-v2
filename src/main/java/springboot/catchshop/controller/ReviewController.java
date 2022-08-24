package springboot.catchshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.ReviewDTO;
import springboot.catchshop.service.ReviewService;

import java.util.List;
import springboot.catchshop.session.SessionConst;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    //모든 상품리뷰 가져오기
    @GetMapping("/{pId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("pId") Long pId){
        List<ReviewDTO> reviewDTOList = reviewService.getListOfProduct(pId);
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    //상품리뷰 추가
    @PostMapping("/{pId}/reviews")
    public ResponseEntity<Long> addReview(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                            @RequestBody ReviewDTO productReviewDTO, @PathVariable("pId") Long productId){
        if (loginUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);//400에러
        }

        Long id = reviewService.register(productReviewDTO, loginUser, productId);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    //상품리뷰 수정
    @PutMapping("/{pId}/reviews/{id}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long id
            , @RequestBody ReviewDTO productReviewDTO){
        reviewService.modify(productReviewDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    //상품리뷰 삭제
    @DeleteMapping("/{pId}/reviews/{id}")
    public ResponseEntity<Long> removeReview(@PathVariable Long id){
        reviewService.remove(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
