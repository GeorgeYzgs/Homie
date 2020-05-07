package com.spring.group.models.user.oauth2;

import java.util.Map;

/**
 * @author George.Giazitzis
 */
public abstract class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) throws Exception {
        AuthProvider authProvider = AuthProvider.valueOf(registrationId.toUpperCase());
        switch (authProvider) {
            case GOOGLE:
                return new GoogleOAuth2(attributes);
            case FACEBOOK:
                return new FacebookOAuth2(attributes);
            case GITHUB:
                return new GithubOAuth2(attributes);
            default:
                throw new Exception("Unsupported OAuth2 Login!");
        }
    }
}
