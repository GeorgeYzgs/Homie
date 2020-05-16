package com.spring.group.models.user.oauth2;

import java.util.Map;

/**
 * @author George.Giazitzis
 * A class that extends oauthuserinfo to validate oauth with Facebook accounts
 * @see OAuth2UserInfo
 */
public class FacebookOAuth2 extends OAuth2UserInfo {

    public FacebookOAuth2(Map<String, Object> attributes) {
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
        if (attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            if (pictureObj.containsKey("data")) {
                Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
                if (dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }
}
