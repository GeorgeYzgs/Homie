package com.spring.group.models.user.oauth2;

import java.util.Map;

/**
 * @author George.Giazitzis
 * A class that extends oauthuserinfo to validate oauth with GitHub accounts
 * @see OAuth2UserInfo
 */
public class GithubOAuth2 extends OAuth2UserInfo {

    public GithubOAuth2(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return (String) attributes.get("login");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
