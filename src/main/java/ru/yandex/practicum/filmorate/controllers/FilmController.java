package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@Slf4j
public class FilmController {
    private HashMap<Long, Film> films = new HashMap<>();
    final static LocalDate BORN_FILMS = LocalDate.of(1895, Month.DECEMBER, 28);
    private long filmId = 0;

    @GetMapping("/films")
    public ArrayList<Film> getFilms() {
        return new ArrayList(films.values());
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        save(film);
        log.info("Добавлен новый фильм '{}'",film.getName());
        return film;
    }

    private void validate(Film film) {
        if(film.getDescription().length() > 200) {
            log.error("Введено слишком длинное описание");
            throw new ValidationException("invalid description");
        }
        if(film.getReleaseDate().isBefore(BORN_FILMS)) {
            log.error("Указана дата выпуска фильма раньше Дня рождения кино");
            throw new ValidationException("invalid birthday");
        }
        if(film.getId() < 0) {
            log.error("ID не может быть меньше 0");
            throw new ValidationException("invalid Id");
        }
        if(film.getName().isBlank()) {
            log.error("Фильм должен быть с названием");
            throw new ValidationException("invalid name of film");
        }
        if(film.getDuration() < 0) {
            log.error("Продолжительность фильма не может быть меньше 0");
            throw new ValidationException("invalid duration");
        }

    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        patch(film);
        log.info("Обновлены данные фильма '{}'", film.getName());
        return film;
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
}

