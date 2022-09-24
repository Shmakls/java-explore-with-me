package ru.andrianov.emw.users.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "email should be valid")
    @NotNull(message = "email should be not null")
    private String email;

    @NotNull(message = "name should be not null")
    @NotEmpty(message = "name should be not empty")
    private String name;

}
