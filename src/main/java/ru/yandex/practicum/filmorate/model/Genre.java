package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Genre {
    private long id;
    @NotBlank
    private String name;

    public Genre (long id, String name) {
        this.id = id;
        this.name = name;
    }
}