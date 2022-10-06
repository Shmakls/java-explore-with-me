package ru.andrianov.emw.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInitiatorDto {

    private Long id;

    private String name;

    public UserInitiatorDto(Long id) {
        this.id = id;
    }
}
