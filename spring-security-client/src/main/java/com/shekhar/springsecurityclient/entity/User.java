package com.shekhar.springsecurityclient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "tbl_user",
        uniqueConstraints = @UniqueConstraint(
                name = "user_unique",
                columnNames = "email"
        )
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long userId;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String role;
    public Boolean isEnabled = false;

}
