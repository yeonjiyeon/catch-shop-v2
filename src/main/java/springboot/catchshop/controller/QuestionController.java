package springboot.catchshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.domain.Question;
import springboot.catchshop.domain.QuestionCategory;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.QuestionDto;
import springboot.catchshop.repository.QuestionRepository;
import springboot.catchshop.service.QuestionService;
import springboot.catchshop.session.SessionConst;

import java.io.IOException;
import java.util.List;

/**
 * 문의사항 controller
 * author: 강수민
 */
@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionRepository questionRepository;

    @GetMapping("/products/{id}/questions")
    public String questionForm(@ModelAttribute("questionDto") QuestionDto questionDto,
                                 @PathVariable("id") Long productId) {
        return "make-question";
    }

    // 문의 신규 등록
    @PostMapping("/products/{id}/questions")
    public String createQuestion(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                 @ModelAttribute QuestionDto questionDto, @PathVariable("id") Long productId) throws IOException {
        if (loginUser != null) {
            questionService.saveQuestion(loginUser, productId, questionDto);
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    // question category select-box
    @ModelAttribute("category")
    private QuestionCategory[] questionCategories() {
        return QuestionCategory.values();
    }

    // 문의 상세 조회
    @GetMapping("/questions/{id}")
    public String singleQuestion(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                 @PathVariable("id") Long questionId, Model model) {
        if (loginUser == null) {
            return "login";
        }
        Question question = questionRepository.findById(questionId).orElse(null);
        model.addAttribute("question", question);

        return "single-question";
    }

    // 관리자용 문의사항 전체 조회
    @GetMapping("/questions")
    public String getAllQuestion(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                 Model model) {
        if (loginUser == null) {
            return "login";
        }

        List<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);

        return "admin/questions";
    }
}
