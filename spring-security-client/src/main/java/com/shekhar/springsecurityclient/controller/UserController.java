package com.shekhar.springsecurityclient.controller;

import com.shekhar.springsecurityclient.entity.User;
import com.shekhar.springsecurityclient.entity.VerificationToken;
import com.shekhar.springsecurityclient.event.RegistrationCompleteEvent;
import com.shekhar.springsecurityclient.model.PasswordModel;
import com.shekhar.springsecurityclient.model.UserModel;
import com.shekhar.springsecurityclient.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/registration")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        User user = userService.registerUser(userModel);
        /*
        Event: event to complete the registration
         */
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "Success";
    }
    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "user verified Successfully";
        }
        return result;
    }
    @GetMapping("/resendVerification")
    public String resendVerification(@RequestParam("token") String oldToken, HttpServletRequest request){
        VerificationToken verificationToken =userService.generateVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenEmail(user, applicationUrl(request), verificationToken);
        return "Verification Link has sent";
    }

    @PostMapping("/resetPassword")
    private String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user != null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordTokenForUser(user,token,request);
            url = passwordResetTokenMail(user,token,applicationUrl(request));
        }
        return url;
    }
    @PostMapping("/savePassword")
    private String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")){
            return "invalid";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){

            userService.changePassword(user.get(),passwordModel.getNewPassword());
            return "password reset successfully";
        }else{
            return "invalid Token";
        }
    }

    private String passwordResetTokenMail(User user, String token, String applicationUrl) {
        /* Send email to user after link crated
                */
        String url = applicationUrl+"/savePassword?token="+token;
        /*
        Email: Send verification email
         */
        log.info("Click the link to Reset your password : {}",url);
        return url;
     }

    private void resendVerificationTokenEmail(User user, String applicationUrl ,VerificationToken verificationToken) {
        /*
        Send email to user after link crated
         */
        String url = applicationUrl+"/verifyRegistration?token="+verificationToken.getToken();
        /*
        Email: Send verification email
         */
        log.info("Click the link to verify your url : {}",url);
    }



    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
