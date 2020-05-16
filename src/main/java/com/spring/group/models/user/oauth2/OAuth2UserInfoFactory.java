package com.spring.group.models.user.oauth2;

import com.spring.group.exceptions.InvalidAuthProviderException;
import com.spring.group.models.user.AuthProvider;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * @author George.Giazitzis
 * A factory to create an oauthuser based on the type of social network they are using.
 * Leverages the fact that every network attaches its name inside the response JSON,
 * which is indentified to differentiate between the social networks to parse their JSON accordingly,
 * to extract the requested data/
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
