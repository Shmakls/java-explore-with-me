package ru.andrianov.emw.users.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {

    private Long id;

    @Email
    @Length(max = 50)
    private String email;

    @NotBlank
    @Length(max = 50)
    private String name;

}
