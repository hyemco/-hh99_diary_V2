package com.sparta.springweb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.springweb.dto.SignupRequestDto;
import com.sparta.springweb.security.UserDetailsImpl;
import com.sparta.springweb.service.KakaoUserService;
import com.sparta.springweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(SignupRequestDto requestDto, Model model) {
        if (userService.registerUser(requestDto).equals("")) {
            return "login";
        } else {
            model.addAttribute("errortext", userService.registerUser(requestDto));
            return "signup";
        }
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    // 로그인 에러 문구 표시
    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    // 카카오 회원 가입
    @GetMapping("user/kakao/callback")
    public String kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        kakaoUserService.kakaoLogin(code);
        return "redirect:/";
    }

    // 유저 로그인 체크
    @GetMapping("/api/userCheck")
    public String userCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            return userDetails.getUser().getUsername();
        }
        return "";
    }
}