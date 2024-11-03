package com.busanit501.springproject3.lhs.controller;

import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String updateUserInfo(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute User updatedUser) {
        if (userDetails != null) {
            myPageService.updateUserInfo(userDetails.getUsername(), updatedUser);
            return "redirect:/users/mypage";
        } else {
            return "redirect:/users/login";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("newPassword") String newPassword) {
        if (userDetails != null) {
            myPageService.changePassword(userDetails.getUsername(), newPassword);
            return "redirect:/users/mypage?passwordChangeSuccess=true";
        } else {
            return "redirect:/users/login";
        }
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            myPageService.deleteUserByUsername(username);
            return "redirect:/users/login?accountDeleted=true";
        } else {
            return "redirect:/users/login";
        }
    }
}
