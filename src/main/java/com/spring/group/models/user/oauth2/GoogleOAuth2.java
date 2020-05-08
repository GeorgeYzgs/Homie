package com.spring.group.models.user.oauth2;

import java.util.Map;

/**
 * @author George.Giazitzis
 */
public class GoogleOAuth2 extends OAuth2UserInfo {

    public GoogleOAuth2(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}
