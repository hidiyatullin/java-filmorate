package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @JsonIgnore
    Set<Long> userIds = new HashSet<>();
}
