package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Mpa {
    private long id;
    @NotBlank
    private String name;

    public Mpa(long id) {
        this.id = id;
    }

    public Mpa(long id, String name) {
        this.id = id;
        this.name = name;
    }
}