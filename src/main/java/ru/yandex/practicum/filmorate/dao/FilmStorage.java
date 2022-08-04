package ru.yandex.practicum.filmorate.dao;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

public interface FilmStorage {
    Film saveFilm(@Valid @RequestBody Film film);

    void update(Film film);
}
