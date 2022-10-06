package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
public class Film {
    private long id;
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private List<Genre> genres;
    private Mpa mpa;
    private int rate; // он ещё нужен?
    @Positive
    private int duration;
    @JsonIgnore
    Set<Long> userIds = new HashSet<>();
}
