package springboot.catchshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.dto.*;
import springboot.catchshop.domain.Role;
import springboot.catchshop.domain.User;
import springboot.catchshop.service.MailService;
import springboot.catchshop.service.UserService;
import springboot.catchshop.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

// User Controller
// author: 강수민, created: 22.02.01
// last modified: 22.04.20
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    // 카카오 로그인
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(String code, HttpServletRequest request) {
        User loginUser = userService.kakaoLogin(code);

        // 세션이 있으면 있는 세션을 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
        session.setAttribute(SessionConst.ROLE, loginUser.getRole()); // 사용자 권한 정보
        session.setAttribute(SessionConst.ID, loginUser.getId().toString()); // 사용자 ID

        return "redirect:/";
    }

    // 회원 전체 조회 - 관리자 기능
    @GetMapping("/users")
    public String getAllUsers(@ModelAttribute("joinDto") JoinDto form) {
        return "join";
    }

    // 회원 가입
    @PostMapping("/users")
    public String join(@Valid @ModelAttribute JoinDto form, BindingResult result) {
        if (result.hasErrors()) {
            return "join";
        }

        String encodedPassword = passwordEncoder.encode(form.getPassword());

        JoinDto joinDto = new JoinDto(form.getLoginId(), encodedPassword,
                form.getName(), form.getTelephone(), form.getEmail(),
                form.getRoad(), form.getDetail(), form.getPostalcode(), form.getRole());

        User user = joinDto.toEntity();
        userService.join(user);
        return "redirect:/";
    }

    // 회원 가입 페이지 - 권한 설정 라디오 박스 값 전달
    @ModelAttribute("roles")
    public Role[] roles() {
        return Role.values();
    }

    // 회원 정보 상세 조회
    @GetMapping("/users/{id}")
    public String getOneUser(@ModelAttribute("updateUserDto") UpdateUserDto form,
                             @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                             Model model, @PathVariable String id) {
        model.addAttribute("user", loginUser);
        return "mypage"; // templates/mypage.html 렌더링
    }

    // 회원 정보 수정
    @PutMapping("/users/{id}")
    public String updateUser(@Valid @ModelAttribute UpdateUserDto form, BindingResult result,
                             @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                             @PathVariable String id) {

        if (result.hasErrors()) {
            return "mypage";
        }

        userService.updateUser(loginUser, form);
        return "redirect:/";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("loginDto") LoginDto form) {
        return "login";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginDto form, BindingResult bindingResult,
                        HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        LoginDto loginDto = new LoginDto(form.getLoginId(), form.getPassword());
        User loginUser = userService.login(loginDto.getLoginId(), loginDto.getPassword());

        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login";
        }

        // 세션이 있으면 있는 세션을 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
        session.setAttribute(SessionConst.ROLE, loginUser.getRole()); // 사용자 권한 정보
        session.setAttribute(SessionConst.ID, loginUser.getId().toString()); // 사용자 ID

        return "redirect:/";
    }

    /**
     * 로그아웃
     * author: 강수민
     * last modified: 22.02.08
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    /**
     * 아이디 찾기
     * author: 강수민
     * last modified: 22.02.08
     */
    @GetMapping("/find-id")
    public String findId(@ModelAttribute("findIdDto") FindIdDto form) {
        return "find-id"; // templates/find-id.html 렌더링
    }

    @PostMapping("/find-id")
    public String findId(@Valid @ModelAttribute FindIdDto form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "find-id";
        }

        User user = userService.findId(form.getName(), form.getTelephone());

        if (user == null) {
            bindingResult.reject("findIdFail", "일치하는 사용자가 없습니다.");
            return "find-id";
        }

        model.addAttribute("userId", user.getLoginId());
        return "find-id";
    }

    /**
     * 비밀번호 찾기
     * author: 강수민
     * last modified: 22.02.08
     */
    @GetMapping("/find-pw")
    public String findPw(@ModelAttribute("findPwDto") FindPwDto form) {
        return "find-pw"; // templates/find-pw.html 렌더링
    }

    @PostMapping("/find-pw")
    public String findPw(@Valid @ModelAttribute FindPwDto form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "find-pw";
        }

        User user = userService.findPw(form.getLoginId());

        if (user == null) {
            bindingResult.reject("findPwFail", "일치하는 사용자가 없습니다.");
            return "find-pw";
        }

        // 임시 비밀번호 생성 및 저장
        String newPassword = userService.updatePw(user);

        // 임시 비밀번호 발급 메일 전송
        MailDto mailDto = new MailDto(form.getEmail(),
                "캐치샵 임시 비밀번호 발급 메일입니다.",
                "임시 비밀번호는 " + newPassword + " 입니다.");
        String success = mailService.sendMail(mailDto);

        model.addAttribute("success", success);
        return "find-pw";
    }


}
