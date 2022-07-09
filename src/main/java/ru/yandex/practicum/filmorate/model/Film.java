package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    long id;
//    @NotBlank
    @NonNull
    String name;
    String description;
    LocalDate releaseDate;
    @Positive
    int duration;

}
