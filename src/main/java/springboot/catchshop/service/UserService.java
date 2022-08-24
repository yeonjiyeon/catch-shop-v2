package springboot.catchshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Role;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.UpdateUserDto;
import springboot.catchshop.repository.UserRepository;
import springboot.catchshop.security.KakaoOAuth2;
import springboot.catchshop.security.KakaoUserInfo;
import springboot.catchshop.security.UserDetailsImpl;

import java.util.Optional;
import java.util.UUID;

// User Service
// author: 강수민, last modified: 22.02.08
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoOAuth2 kakaoOAuth2;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    /**
     * 회원가입
     */
    @Transactional
    public Long join(User user) {
        // 중복 아이디 검증
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * 아이디 중복 검증
     */
    private void validateDuplicateUser(User user) {
        userRepository.findByLoginId(user.getLoginId())
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 카카오 로그인
     */
    @Transactional
    public User kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            User sameEmailUser = userRepository.findByEmail(email).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 카카오 이메일과 동일한 이메일 회원이 있는 경우
                // 카카오 Id 를 회원정보에 저장
                kakaoUser.updateKakaoId(kakaoId);
            } else {
                // 카카오 정보로 회원가입
                // username = 카카오 nickname
                // password = 카카오 Id + ADMIN TOKEN
                String password = kakaoId + ADMIN_TOKEN;
                String encodedPassword = passwordEncoder.encode(password);
                // ROLE = 사용자
                String role = Role.USER.toString();

                kakaoUser = new User(nickname, encodedPassword, email, role, kakaoId);
            }
            userRepository.save(kakaoUser);
        }

        // 강제 로그인 처리
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return kakaoUser;
    }

    /**
     * 로그인
     * @return null이면 로그인 실패
     */
    @Transactional(readOnly = true)
    public User login(String loginId, String password) {
        return userRepository.findByLoginId(loginId)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(null);

    }

    /**
     * 아이디 찾기
     */
    @Transactional(readOnly = true)
    public User findId(String name, String telephone) {
        return userRepository.findByName(name)
                .filter(u -> u.getTelephone().equals(telephone))
                .orElse(null);
    }

    /**
     * 비밀번호 찾기
     */
    @Transactional(readOnly = true)
    public User findPw(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElse(null);
    }

    /**
     * 임시 비밀번호 생성 및 저장
     */
    @Transactional
    public String updatePw(User user) {
        String uuid = "";
        for (int i = 0; i < 5; i++) {
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
            uuid = uuid.substring(0, 10);
        }
        String encodedPassword = passwordEncoder.encode(uuid);
        user.updatePassword(encodedPassword);
        return encodedPassword;
    }

    /**
     * 회원정보수정
     */
    @Transactional
    public void updateUser(User user, UpdateUserDto form) {
        user.updateUser(form);
        user.updatePassword(passwordEncoder.encode(form.getPassword()));
        // DB 반영
        userRepository.save(user);
    }

    /**
     * 회원탈퇴
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
