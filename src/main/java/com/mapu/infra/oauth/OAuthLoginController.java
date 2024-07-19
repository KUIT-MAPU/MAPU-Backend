package com.mapu.infra.oauth;

import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import com.mapu.global.common.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/oauth")
public class OAuthLoginController {

    @Autowired
    private final OAuthService oAuthService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login/{socialLoginType}")
    public String socialLogin(@PathVariable("socialLoginType") String oauthType, @RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("socialLoginType: {}", oauthType.toUpperCase());
        OAuthUserInfo oAuthUserInfo = oAuthService.oAuthLogin(oauthType.toUpperCase(), code);

        log.info("OAuth login response email info: {}", oAuthUserInfo.email);
        // oAuthUserInfo의 email로 DB에 user가 있는지 확인하고 없으면 추가정보 입력을 위해 세션에 저장
        if(userRepository.existsByEmail(oAuthUserInfo.getEmail())){
            // TODO: 기존에 있던 User이므로 토큰 return (임시로 홈으로 redirect 시킴)
            return "redirect:/";
        }else{
            // 사용자 정보가 DB에 없을 경우 세션에 저장
            HttpSession session = request.getSession();
            session.setAttribute("platform_id", oAuthUserInfo.getSocialId());
            session.setAttribute("email", oAuthUserInfo.getEmail());
            session.setAttribute("platform_name", oAuthUserInfo.getSocialProvider());
            log.info("New user info saved in session: {}", oAuthUserInfo.getEmail());
            return "redirect:/user/signup";
        }
    }

}
