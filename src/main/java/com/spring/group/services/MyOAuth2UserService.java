package com.spring.group.services;

import com.spring.group.exceptions.InvalidAuthProviderException;
import com.spring.group.models.user.AuthProvider;
import com.spring.group.models.user.MyUserDetails;
import com.spring.group.models.user.User;
import com.spring.group.models.user.oauth2.OAuth2UserInfo;
import com.spring.group.models.user.oauth2.OAuth2UserInfoFactory;
import com.spring.group.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Service
public class MyOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        return processOAuth2User(oAuth2UserRequest, super.loadUser(oAuth2UserRequest));
    }

    //TODO Allow users to update through social profile.
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        AuthProvider authProvider = AuthProvider.valueOf(userRequest.getClientRegistration()
                .getRegistrationId().toLowerCase());
        Map<String, Object> userAttributes = oAuth2User.getAttributes();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getUserInfo(authProvider, userAttributes);
        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getAuthProvider().equals(authProvider)) {
                String msg = "You are signed up with " + user.getAuthProvider() + " account." +
                        " Please use your " + user.getAuthProvider() + " account to login.";
                throw new InvalidAuthProviderException(msg);
            }
            return new MyUserDetails(user, userAttributes);
        }
        user = new User(authProvider, userInfo);
        return new MyUserDetails(userRepository.save(user), userAttributes);
    }
}
