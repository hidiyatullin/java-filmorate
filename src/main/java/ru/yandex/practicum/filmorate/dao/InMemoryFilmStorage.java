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
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private final static LocalDate BORN_FILMS = LocalDate.of(1895, Month.DECEMBER, 28);
    private long filmId = 0;

    @Override
    public Film get(long filmId) {
        return films.get(filmId);
    }

    @Override
    public Film saveFilm(@Valid @RequestBody Film film) {
        save(film);
        log.info("Добавлен новый фильм '{}'", film.getName());
        return film;
    }
    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
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

    @Override
    public void addLike(Film film, User user) {
        film.getUserIds().add(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        film.getUserIds().remove(user.getId());
    }
}
