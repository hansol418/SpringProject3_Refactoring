package com.busanit501.springproject3.lhs.controller.api;

import com.busanit501.springproject3.lhs.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/mypage")
@Log4j2
@RequiredArgsConstructor
public class MyPageAPIController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<String> showMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            log.info("User {} is accessing MyPage", userDetails.getUsername());
            return ResponseEntity.ok("Welcome to your MyPage, " + userDetails.getUsername());
        } else {
            log.warn("Unauthenticated user tried to access MyPage");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login to access MyPage.");
        }
    }

    @GetMapping("/confirmDelete")
    public ResponseEntity<String> confirmDelete(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            log.info("User {} is confirming account deletion", userDetails.getUsername());
            return ResponseEntity.ok("You can proceed to delete your account, " + userDetails.getUsername());
        } else {
            log.warn("Unauthenticated user tried to confirm account deletion.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login to confirm account deletion.");
        }
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam("password") String password) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            log.info("User {} is attempting to delete their account.", username);

            boolean isDeleted = myPageService.verifyAndDeleteUser(username, password);
            if (isDeleted) {
                log.info("User {} successfully deleted their account.", username);
                return ResponseEntity.ok("Account successfully deleted. Please login again.");
            } else {
                log.warn("Failed to delete account for user {}: Incorrect password.", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password. Account not deleted.");
            }
        } else {
            log.warn("Unauthenticated user attempted to delete an account.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login to delete your account.");
        }
    }
}
