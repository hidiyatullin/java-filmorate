package ru.yandex.practicum.filmorate.dao;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public interface FilmStorage {

    Film get(long filmId);
    Film saveFilm(@Valid @RequestBody Film film);

    void update(Film film);

    List<Film> getFilms();

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);
}
