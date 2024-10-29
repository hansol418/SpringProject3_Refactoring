package com.busanit501.springproject3.lhs.service;

import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyPageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public void deleteUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userService.deleteUser(user.get().getId());
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
