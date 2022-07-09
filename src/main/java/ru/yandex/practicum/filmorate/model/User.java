package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    int id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    @Past
    LocalDate birthday;

}
