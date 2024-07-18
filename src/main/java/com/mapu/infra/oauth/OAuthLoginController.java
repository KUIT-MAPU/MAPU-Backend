package com.mapu.infra.oauth;

import com.mapu.domain.user.domain.User;
import com.mapu.global.common.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/oauth")
public class OAuthLoginController {

    private final OAuthService oAuthService;

    @GetMapping("/login/{socialLoginType}")
    public String socialLogin(@PathVariable("socialLoginType") String oauthType, @RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("socialLoginType: {}", oauthType.toUpperCase());
        OAuthUserInfo oAuthUserInfo = oAuthService.oAuthLogin(oauthType.toUpperCase(), code);

        log.info("OAuth login response email info: {}", oAuthUserInfo.email);
        // TODO: oAuthUserInfo의 email로 DB에 user가 있는지 확인하고 없으면 DB에 저장
//        User user;
//        try {
//            userRepository.findByEmail(email)
//            user = userService.getUserByEmail(oAuthUserInfo.getEmail());
//            // TODO: 토큰 발급 및 로그인 처리 (주석 처리)
//        } catch (UserException e) {
//            // 사용자 정보가 DB에 없을 경우 세션에 저장
//            HttpSession session = request.getSession();
//            UserDTO tempUser = new UserDTO();
//            tempUser.setEmail(oAuthUserInfo.getEmail());
//            tempUser.setNickname(oAuthUserInfo.getNickname());
//            tempUser.setProfileId(oAuthUserInfo.getProfileId());
//            session.setAttribute("tempUser", tempUser);
//            log.info("Temporary user info saved in session: {}", tempUser);
//        }

        // TODO: 로그인 처리(세션에 토큰이랑 회원정보 저장 후 쿠키) ex : login(HttpServletRequest request, HttpServletResponse response, Member member, String access_token)
        return "redirect:/";
    }

//    private void login(HttpServletRequest request, HttpServletResponse response, Member member, String access_token) {
//        HttpSession session = request.getSession();
//        session.setAttribute(SessionConstants.LOGIN_MEMBER, member);
//        session.setAttribute(SessionConstants.ACCESS_TOKEN, access_token);
//        log.info(session.getId());
//    }

}
