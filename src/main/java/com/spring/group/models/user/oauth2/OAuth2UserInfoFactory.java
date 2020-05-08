package com.spring.group.models.user.oauth2;

import com.spring.group.exceptions.InvalidAuthProviderException;
import com.spring.group.models.user.AuthProvider;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * @author George.Giazitzis
 */
public abstract class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getUserInfo(AuthProvider authProvider, Map<String, Object> attributes) throws ResponseStatusException {
        switch (authProvider) {
            case google:
                return new GoogleOAuth2(attributes);
            case facebook:
                return new FacebookOAuth2(attributes);
            case github:
                return new GithubOAuth2(attributes);
            default:
                throw new InvalidAuthProviderException("Unsupported OAuth2 Login!");
        }
    }
}
