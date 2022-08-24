package springboot.catchshop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.Role;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.JoinDto;
import springboot.catchshop.dto.UpdateUserDto;
import springboot.catchshop.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

// author: 강수민
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired UserService userService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    void 회원가입() throws Exception {
        // given
        JoinDto form = new JoinDto("userA", "A", "강유저", "010-1234-5678",
                "건강시 행복구 사랑동", "부자아파트", "12345",
                "user1@catchshop.ac.kr", Role.USER.toString());

        User user = form.toEntity();

        // when
        Long savedId = userService.join(user);

        // then
        assertEquals(user, userRepository.findById(savedId).get());
    }

    @Test
    void 아이디중복확인() throws Exception {
        // given
        JoinDto userA = new JoinDto("userA", "A", "강유저", "010-1234-5678", "userA@catchshop.ac.kr",
                "건강시 행복구 사랑동", "부자아파트", "12345", Role.USER.toString());

        JoinDto userB = new JoinDto("userA", "A", "강유저", "010-1234-5678", "userB@catchshop.ac.kr",
                "건강시 행복구 사랑동", "부자아파트", "12345", Role.USER.toString());

        // when
        userService.join(userA.toEntity());
        try {
            userService.join(userB.toEntity());
        } catch (IllegalStateException e) {
            return;
        }

        // then
        Assertions.fail("아이디 중복시 가입 불가");
    }

    @Test
    void 회원정보수정() throws Exception {

        // given
        Address address1 = new Address("road1", "detail1", "11111");
        User user1 = new User("user1", passwordEncoder.encode("1"), "user1",
                "01012345678", "user1@catchshop.ac.kr", address1, Role.USER.toString());

        // 수정 전
        assertEquals("01012345678", user1.getTelephone());
        assertThat(passwordEncoder.matches("1", user1.getPassword())).isTrue();

        // when
        UpdateUserDto dto = new UpdateUserDto("2", "01087654321",
                "건강시 행복구 사랑동", "부자아파트", "12345");
        userService.updateUser(user1, dto);

        // then - 수정 후
        assertEquals("01087654321", user1.getTelephone());
        assertThat(passwordEncoder.matches("2", user1.getPassword())).isTrue();

    }

}