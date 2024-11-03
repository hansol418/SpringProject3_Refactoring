package com.busanit501.springproject3.lhs.service;

import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    // 유저 정보 업데이트
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

    // 비밀번호 변경
    public void changePassword(String username, String newPassword) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // 계정 삭제
    public void deleteUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
