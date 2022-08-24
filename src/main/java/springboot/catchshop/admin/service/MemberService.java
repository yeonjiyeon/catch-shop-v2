package springboot.catchshop.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import springboot.catchshop.domain.Role;
import springboot.catchshop.domain.User;
import springboot.catchshop.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;

    // 일반 회원 전체 조회 + 페이징
    @Transactional(readOnly = true)
    public Page<User> getAllMembersWithPaging(int startAt) {
        // 최신순 조회
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(startAt, 10, Sort.by(sorts));
        return userRepository.findByLoginIdNotNull(pageable);
    }

    // 카카오 회원 전체 조회 + 페이징
    @Transactional(readOnly = true)
    public Page<User> getAllKakaoMembersWithPaging(int startAt) {
        // 최신순 조회
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(startAt, 10, Sort.by(sorts));
        return userRepository.findByLoginIdNull(pageable);
    }
}
