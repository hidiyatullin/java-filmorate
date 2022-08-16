package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    private final static LocalDate BORN_FILMS = LocalDate.of(1895, Month.DECEMBER, 28);

    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        return filmService.get(filmId);
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        Film saved = filmService.save(film);
        log.info("Добавлен новый фильм '{}'", saved.getName());
        return saved;
    }

    private void validate(Film film) {
        if (film.getDescription().length() > 200) {
            log.error("Введено слишком длинное описание");
            throw new ValidationException("invalid description");
        }
        if (film.getReleaseDate().isBefore(BORN_FILMS)) {
            log.error("Указана дата выпуска фильма раньше Дня рождения кино");
            throw new ValidationException("invalid birthday");
        }
        if (film.getId() < 0) {
            log.error("ID не может быть меньше 0");
            throw new NotFoundException("invalid Id");
        }
        if (film.getName().isBlank()) {
            log.error("Фильм должен быть с названием");
            throw new ValidationException("invalid name of film");
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма не может быть меньше 0");
            throw new ValidationException("invalid duration");
        }

    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        filmService.update(film);
        log.info("Обновлены данные фильма '{}'", film.getName());
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Запрос на вывод " + count + " самых популярных фильмов");
        return filmService.getFilmLikes(count);
    }
}

