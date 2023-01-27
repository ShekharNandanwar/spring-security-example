package com.shekhar.springsecurityclient.service;

import com.shekhar.springsecurityclient.entity.User;
import com.shekhar.springsecurityclient.entity.VerificationToken;
import com.shekhar.springsecurityclient.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface UserService {
    public User registerUser(UserModel userModel);

    public void saveVerificationTokenForUser(String token, User user);

    public String validateVerificationToken(String token);

    public VerificationToken generateVerificationToken(String oldToken);

    public User findUserByEmail(String email);

    public void createPasswordTokenForUser(User user, String token, HttpServletRequest request);

    public String validatePasswordResetToken(String token);

    public Optional<User> getUserByPasswordResetToken(String token);

    public void changePassword(User user, String newPassword);
}
