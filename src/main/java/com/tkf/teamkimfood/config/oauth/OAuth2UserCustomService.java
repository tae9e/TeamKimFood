package com.tkf.teamkimfood.config.oauth;
import com.tkf.teamkimfood.domain.User;
import com.tkf.teamkimfood.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    private User saveOrUpdate(OAuth2User oAuth2User){
        Map<String, Object> attritues = oAuth2User.getAttributes();

        String email=(String)attritues.get("email");
        String name=(String)attritues.get("name");

        User user = userRepository.findByMemberEmail(email)
                .map(entity->entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .password("test") //temporary...
                        .build());

        return userRepository.save(user);

    }
}