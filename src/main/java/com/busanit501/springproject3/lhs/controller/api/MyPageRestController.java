package com.busanit501.springproject3.lhs.controller.api;

import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/mypage")
@Log4j2
@RequiredArgsConstructor
public class MyPageRestController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<User> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            Optional<User> user = myPageService.getUserByUsername(username);
            return user.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editUserField(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody Map<String, String> updates) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            boolean isUpdated = myPageService.updateUserField(username, updates);

            if (isUpdated) {
                return ResponseEntity.ok("User information updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user information.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to edit user information.");
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody Map<String, String> passwords) {
        if (userDetails != null) {
            String currentPassword = passwords.get("currentPassword");
            String newPassword = passwords.get("newPassword");
            String confirmNewPassword = passwords.get("confirmNewPassword");

            if (!newPassword.equals(confirmNewPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New passwords do not match.");
            }

            boolean isChanged = myPageService.changePassword(userDetails.getUsername(), currentPassword, newPassword);
            if (isChanged) {
                return ResponseEntity.ok("Password changed successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect current password.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to change your password.");
        }
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam("password") String password) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            boolean isDeleted = myPageService.verifyAndDeleteUser(username, password);
            if (isDeleted) {
                return ResponseEntity.ok("Account deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to delete your account.");
        }
    }
}
