package com.busanit501.springproject3.lhs.controller.api;



import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.service.ImageUploadService;
import com.busanit501.springproject3.lhs.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/public/users")
@Log4j2
public class UserRestPublicController {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ImageUploadService imageUploadService;

    @PostMapping
    public ResponseEntity<User> createUser( @RequestPart("user") User user,
                                            @RequestParam(value = "profileImage", required = false) MultipartFile file) {
        try {
            log.info("image 확인 : " + file);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User createdUser = userService.createUser(user);

            if (file !=null && !file.isEmpty()) {
                userService.saveProfileImage(createdUser.getId(), file);
            }

            return ResponseEntity.ok(createdUser);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save user or profile image", e);
        }
    }


}
