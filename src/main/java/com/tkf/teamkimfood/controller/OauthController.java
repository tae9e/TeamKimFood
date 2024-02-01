package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.config.jwt.AuthTokensGenerator;
import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.domain.status.MemberRole;
import com.tkf.teamkimfood.dto.LoginCredentialsVo;
import com.tkf.teamkimfood.infra.KakaoApiClient;
import com.tkf.teamkimfood.infra.KakaoLoginParams;
import com.tkf.teamkimfood.service.MemberService;
import com.tkf.teamkimfood.service.OAuthLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/public")
public class OauthController {

    private final OAuthLoginService oAuthLoginService;
    private final KakaoApiClient kakaoApiClient;
    private final AuthTokensGenerator authTokensGenerator;
    private final MemberService memberService;


    public OauthController(OAuthLoginService oAuthLoginService, KakaoApiClient kakaoApiClient, AuthTokensGenerator authTokensGenerator, MemberService memberService) {
        this.kakaoApiClient = kakaoApiClient;
        this.oAuthLoginService = oAuthLoginService;
        this.authTokensGenerator = authTokensGenerator;
        this.memberService = memberService;
    }

    @GetMapping("/auth/kakao/login")
    public void KakaoOauthTest(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoApiClient.getAuthorizeUrl());
    }

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestParam("code") String code) {

        KakaoLoginParams kakaoLoginParams = new KakaoLoginParams();
        log.info("KakaoParams : {}", kakaoLoginParams);
        //로그는 (+) 쓰지말고 아래처럼 "" 안에는 {} 로 변수 위치 잡아주고 (,) 뒤에다가 해당 위치에 넣을 변수 지정해주면 됨
        log.info("code : {}", code);
        kakaoLoginParams.setAuthorizationCode(code);

        String accessToken = kakaoApiClient.requestAccessToken(kakaoLoginParams);
        OAuthInfoResponse userInfo = kakaoApiClient.requestOauthInfo(accessToken);
        log.info("{}", userInfo.getEmail());

        //DB에 User정보 담기
        Long userId = oAuthLoginService.findOrCreateMember(userInfo);
        String redirectUrl = "http://localhost:3000/boardList"; // 리디렉션할 페이지의 URL
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("accessToken", accessToken);
        responseBody.put("userInfo", userInfo);
        responseBody.put("redirectUrl", redirectUrl); // 리디렉션 URL을 응답에 추가
        // 여기서 리다이렉트 하지말고 토큰 값이랑 프론트에서 필요한 사용자 정보를 보내주면 됨
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/redirect")
    public ResponseEntity<Void> performRedirection(@RequestParam String redirectUrl) {
        // 리디렉션을 수행하는 302 응답 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // HttpStatus.FOUND: 302 응답 코드


    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCredentialsVo loginCredentialsVo) {
        try {
            // 사용자 정보 가져오기
            UserDetails userDetails = memberService.loadUserByUsername(loginCredentialsVo.getUsername());

            // 사용자가 관리자인지 확인
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            // 사용자 인증 성공 시 처리
            Long memberForToken = memberService.findMemberForLogin(loginCredentialsVo.getUsername(), loginCredentialsVo.getPassword());
            AuthTokens tokens = authTokensGenerator.generate(memberForToken);
            log.info("토큰 : " + tokens.getAccessToken());


            // JSON 객체로 토큰을 감싸 반환
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", tokens.getAccessToken());
            tokenMap.put("isAdmin",Boolean.toString(isAdmin));

            return ResponseEntity.ok(tokenMap);
        } catch (UsernameNotFoundException e) {
            // 사용자를 찾을 수 없는 경우 에러 처리 또는 다른 로직 수행
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
        } catch (BadCredentialsException e) {
            // 잘못된 자격 증명인 경우 에러 처리 또는 다른 로직 수행
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저이름 혹은 비밀번호가 다릅니다.");
        }
    }
}