package com.mapu.infra.oauth;

import com.mapu.infra.oauth.jwt.dto.JwtUserDto;
import com.mapu.infra.oauth.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtUtil jwtUtil;

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

        String role = "ROLE_USER"; //임시 role //TODO: db에서 유저 role 값 받아와서 넣기
        JwtUserDto jwtUserDto = new JwtUserDto();
        jwtUserDto.setName(oAuthUserInfo.email);
        jwtUserDto.setRole(role);
        response.addCookie(jwtUtil.createAccessJwtCookie(jwtUserDto));
        response.addCookie(jwtUtil.createRefreshJwtCookie(jwtUserDto));
        return "redirect:/";
    }

//    private void login(HttpServletRequest request, HttpServletResponse response, User user, String access_token) {
//        HttpSession session = request.getSession();
//        session.setAttribute(SessionConstants.LOGIN_MEMBER, user);
//        session.setAttribute(SessionConstants.ACCESS_TOKEN, access_token);
//        log.info(session.getId());
//    }

}
