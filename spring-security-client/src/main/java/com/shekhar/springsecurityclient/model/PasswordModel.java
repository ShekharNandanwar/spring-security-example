package com.shekhar.springsecurityclient.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class PasswordModel {
    private Long id;
    private String email;
    private String oldPassword;
    private String newPassword;
}
