package com.busanit501.springproject3.lhs.controller;

import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/users/mypage")
@Log4j2
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public String showMyPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            myPageService.getUserByUsername(username).ifPresent(user -> model.addAttribute("user", user));
            return "user/mypage";
        } else {
            return "redirect:/users/login";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            myPageService.getUserByUsername(username).ifPresent(user -> model.addAttribute("user", user));
            return "user/edit-mypage";
        } else {
            return "redirect:/users/login";
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editUserField(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, String> updates) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            boolean isUpdated = false;

            // email, address, phone 필드 변경 가능하도록 처리
            if (updates.containsKey("email") || updates.containsKey("address") || updates.containsKey("phone")) {
                isUpdated = myPageService.updateUserField(username, updates);
            }

            if (isUpdated) {
                return ResponseEntity.ok("수정되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("업데이트 실패.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 시도하세요.");
        }
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm() {
        return "user/change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmNewPassword") String confirmNewPassword,
                                 Model model) {
        if (userDetails != null) {
            if (!newPassword.equals(confirmNewPassword)) {
                model.addAttribute("error", "새 비밀번호가 일치하지 않습니다.");
                return "user/change-password";
            }

            boolean isChanged = myPageService.changePassword(userDetails.getUsername(), currentPassword, newPassword);
            if (isChanged) {
                model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
                return "redirect:/users/mypage";
            } else {
                model.addAttribute("error", "현재 비밀번호가 올바르지 않습니다.");
                return "user/change-password";
            }
        } else {
            return "redirect:/users/login";
        }
    }

    @GetMapping("/deleteAccount")
    public String showDeleteAccountPage() {
        return "user/confirm-delete";
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("password") String password, Model model) {
        if (userDetails != null) {
            boolean isDeleted = myPageService.verifyAndDeleteUser(userDetails.getUsername(), password);
            if (isDeleted) {
                return "redirect:/users/login?accountDeleted=true";
            } else {
                model.addAttribute("error", "비밀번호가 올바르지 않습니다");
                return "user/confirm-delete";
            }
        } else {
            return "redirect:/users/login";
        }
    }
}
