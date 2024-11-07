package com.busanit501.springproject3.lhs.service;

import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MyPageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 유저 정보 조회
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 유저 정보 업데이트 (전체 정보 업데이트)
    public User updateUserInfo(String username, User updatedUserInfo) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(updatedUserInfo.getEmail());
            user.setAddress(updatedUserInfo.getAddress());
            user.setPhone(updatedUserInfo.getPhone());
            user.setName(updatedUserInfo.getName());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // 특정 필드 업데이트 (email, address, phone 필드를 업데이트)
    public boolean updateUserField(String username, Map<String, String> updates) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // 주어진 필드와 값을 기반으로 업데이트 수행
            updates.forEach((field, value) -> {
                switch (field) {
                    case "email":
                        user.setEmail(value);
                        break;
                    case "address":
                        user.setAddress(value);
                        break;
                    case "phone":
                        user.setPhone(value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid field: " + field);
                }
            });

            // 업데이트된 정보를 저장
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // 비밀번호 변경 (현재 비밀번호 확인 포함)
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // 현재 비밀번호 확인
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true; // 비밀번호 변경 성공
            }
        }
        return false; // 현재 비밀번호가 올바르지 않음
    }

    // 비밀번호 확인 후 계정 삭제
    public boolean verifyAndDeleteUser(String username, String password) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                userRepository.delete(user);
                return true; // 삭제 성공
            }
        }
        return false; // 비밀번호 불일치 또는 사용자 없음
    }
}
