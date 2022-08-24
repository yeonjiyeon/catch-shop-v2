package springboot.catchshop.admin.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import springboot.catchshop.domain.Question;
import springboot.catchshop.domain.Review;
import springboot.catchshop.domain.User;
import springboot.catchshop.repository.ReviewRepository;
import springboot.catchshop.service.ReviewService;
import springboot.catchshop.session.SessionConst;

@Controller
@RequiredArgsConstructor
public class ReviewControllerForAdmin {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    // 관리자 리뷰 전체 조회
    @GetMapping("/reviews/admin")
    public String getAllReviews(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
        Model model) {
        if (loginUser == null) {
            return "login";
        }

        List<Review> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);

        return "admin/reviews";
    }
}
