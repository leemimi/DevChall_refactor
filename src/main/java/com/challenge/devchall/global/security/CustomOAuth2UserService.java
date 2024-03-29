package com.challenge.devchall.global.security;

import com.challenge.devchall.domain.member.entity.Member;
import com.challenge.devchall.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        String oauthId;
        String email;
        String nickname;

        if (providerTypeCode.equals("NAVER")) {
            oauthId = ((Map<String, String>) oAuth2User.getAttributes().get("response")).get("id");
            email = ((Map<String, String>) oAuth2User.getAttributes().get("response")).get("email");
            nickname = ((Map<String, String>) oAuth2User.getAttributes().get("response")).get("nickname");
        } else {
            oauthId = oAuth2User.getName();
            email = oAuth2User.getName();
            nickname = oAuth2User.getName();
        }

        if (oauthId.length() > 8) {
            oauthId = providerTypeCode + "__%s".formatted(oauthId.substring(0,8));
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();

        Member member = memberService.whenSocialLogin(providerTypeCode, oauthId, email, nickname).getData();

        return new CustomOAuth2User(member.getLoginID(), member.getPassword(), member.getGrantedAuthorities());
    }
}

class CustomOAuth2User extends User implements OAuth2User {

    public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}