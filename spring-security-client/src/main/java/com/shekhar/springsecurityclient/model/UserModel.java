package com.shekhar.springsecurityclient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String matchingPassword;
}
