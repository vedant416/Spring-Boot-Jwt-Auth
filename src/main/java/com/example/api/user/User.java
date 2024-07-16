package com.example.api.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue
    @Column
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
