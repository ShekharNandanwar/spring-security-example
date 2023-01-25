package com.shekhar.springsecurityclient.controller;

import com.shekhar.springsecurityclient.entity.User;
import com.shekhar.springsecurityclient.entity.VerificationToken;
import com.shekhar.springsecurityclient.event.RegistrationCompleteEvent;
import com.shekhar.springsecurityclient.model.UserModel;
import com.shekhar.springsecurityclient.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
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

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
