package dev.highright96.server.loginUserDetails.security.oauth2;

import dev.highright96.server.loginUserDetails.domain.User;
import dev.highright96.server.loginUserDetails.repository.UserRepository;
import dev.highright96.server.loginUserDetails.security.UserPrincipal;
import dev.highright96.server.loginUserDetails.security.oauth2.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 로그인 시도
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글, 네이버, 카카오 로그인 구분값
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 징행시 키가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        if (!StringUtils.hasText(attributes.getEmail())) {
            throw new OAuth2AuthenticationException("OAuth2 이메일이 없습니다.");
        }

        User user = saveOrUpdate(attributes, registrationId, userNameAttributeName);
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User saveOrUpdate(OAuthAttributes attributes, String registrationId, String userNameAttributeName) {
        User user = userRepository.findUserByEmail(attributes.getEmail())
                .orElseGet(() ->
                        attributes.toEntity(registrationId, userNameAttributeName)
                );

        user.setName(attributes.getName());
        user.setImageUrl(attributes.getImageUrl());
        return userRepository.save(user);
    }
}
