package dev.highright96.server.loginUserDetails.security.oauth2;

import dev.highright96.server.loginUserDetails.domain.Role;
import dev.highright96.server.loginUserDetails.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String imageUrl;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String imageUrl) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .imageUrl((String) attributes.get("imageUrl"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(String registrationId, String userNameAttributeName) {
        return User.builder()
                .name(name)
                .email(email)
                .provider(AuthProvider.valueOf(registrationId))
                .providerId(userNameAttributeName)
                .role(Role.ROLE_USER)
                .build();
    }

}
