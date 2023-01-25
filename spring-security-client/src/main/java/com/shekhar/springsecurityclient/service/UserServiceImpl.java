package com.shekhar.springsecurityclient.service;

import com.shekhar.springsecurityclient.entity.User;
import com.shekhar.springsecurityclient.entity.VerificationToken;
import com.shekhar.springsecurityclient.model.UserModel;
import com.shekhar.springsecurityclient.repository.UserRepository;
import com.shekhar.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public User registerUser(UserModel userModel) {
        User user = User.builder()
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .email(userModel.getEmail())
                .role("User")
                .password(passwordEncoder.encode(userModel.getPassword()))
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null){
            return "invlid";
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime())<=0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setIsEnabled(true);
        return "valid";
    }
}
