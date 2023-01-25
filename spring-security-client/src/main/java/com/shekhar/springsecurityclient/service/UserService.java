package com.shekhar.springsecurityclient.service;

import com.shekhar.springsecurityclient.entity.User;
import com.shekhar.springsecurityclient.entity.VerificationToken;
import com.shekhar.springsecurityclient.model.UserModel;

public interface UserService {
    public User registerUser(UserModel userModel);

    public void saveVerificationTokenForUser(String token, User user);

    public String validateVerificationToken(String token);
}
