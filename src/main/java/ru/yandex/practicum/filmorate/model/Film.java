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
    private long id;
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;

}
