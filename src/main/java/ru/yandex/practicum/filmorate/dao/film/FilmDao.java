package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface FilmDao {

    Optional<Film> getById(long filmId);
    Film create(@Valid @RequestBody Film film);

    Optional<Film> update(Film film);

    List<Film> getFilms();

//    void delete(long id); //удаление фильма не было

    List<Film> getPopular(int count);

}