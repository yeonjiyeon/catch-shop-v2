package springboot.catchshop.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.admin.repository.AnswerRepository;
import springboot.catchshop.admin.service.AnswerService;
import springboot.catchshop.domain.Answer;
import springboot.catchshop.domain.User;
import springboot.catchshop.session.SessionConst;

// Answer Controller
// author: 강수민, created: 22.03.15
// last modified: 22.04.21
@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final AnswerRepository answerRepository;

    // 답변 신규 등록
    @PostMapping("/questions/{id}/answers")
    public String createAnswer(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                               @RequestParam("new-content") String content, @PathVariable("id") Long questionId) {
        if (loginUser != null) {
            answerService.createAnswer(loginUser, questionId, content);
            return "redirect:/questions";
        } else {
            return "redirect:/login";
        }
    }

    // 답변 수정
   @PutMapping("/answers/{id}")
   public String updateAnswer(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                              @PathVariable("id") Long answerId, @RequestParam("new-content") String content) {
       Answer answer = answerRepository.findById(answerId).orElseThrow( () -> new IllegalArgumentException("답변이 존재하지 않습니다."));
       if (loginUser != null) {
           answerService.updateAnswer(answerId, content);
           return "redirect:/questions/"+answer.getQuestion().getId();
       } else {
           return "redirect:/login";
       }
   }

    // 답변 삭제
    @DeleteMapping("/answers/{id}")
    public String deleteAnswer(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, @PathVariable("id") Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow( () -> new IllegalArgumentException("답변이 존재하지 않습니다."));
        if (loginUser != null) {
            answerService.deleteAnswer(answerId);
            return "redirect:/questions/"+answer.getQuestion().getId();
        } else {
            return "redirect:/login";
        }
    }

    // 답변 상세 조회
    @GetMapping("/answers/{id}")
    public String getSingleAnswer(@PathVariable("id") Long answerId) {
        return "/";
    }

    // 답변 전체 조회
    @GetMapping("questions/{id}/answers")
    public String getAllAnswers(@PathVariable("id") Long questionId) {
        return "/";
    }

}
