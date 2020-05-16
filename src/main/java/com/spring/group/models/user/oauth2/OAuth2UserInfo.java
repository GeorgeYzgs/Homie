package com.spring.group.models.user.oauth2;

import java.util.Map;

/**
 * @author George.Giazitzis
 * An abstract class that is used as a base for a factory design pattern
 * for oauth authorization with multiple social netowrks.
 * Requests the user's name email and potential avatar upon authentication.
 * @see OAuth2UserInfoFactory
 */
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
