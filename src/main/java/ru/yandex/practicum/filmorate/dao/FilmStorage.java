package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component
public class FilmStorage {

    private HashMap<Long, Film> films = new HashMap<>();
    private final static LocalDate BORN_FILMS = LocalDate.of(1895, Month.DECEMBER, 28);
    private long filmId = 0;

    public Film get(long filmId) {
        return films.get(filmId);
    }

    public Film saveFilm(@Valid @RequestBody Film film) {
        save(film);
        log.info("Добавлен новый фильм '{}'", film.getName());
        return film;
    }

    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public void update(Film film) {
        patch(film);
    }

    private void patch(Film film) {
        boolean lost = true;
        for (Film oldFilm : films.values()) {
            if (oldFilm.getId() == film.getId()) {
                lost = false;
                films.remove(oldFilm.getId());
                films.put(film.getId(), film);
            }
        } if (lost) {
            log.error("Нельзя обновить фильм, который ещё не сохранен");
            throw new ValidationException("Нет сохраненного фильма с id " + film.getId());
        }
    }

    private void save(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(film.getId(), film);
    }


    public void addLike(Film film, User user) {
        film.getUserIds().add(user.getId());
    }

    public void deleteLike(Film film, User user) {
        film.getUserIds().remove(user.getId());
    }
}
