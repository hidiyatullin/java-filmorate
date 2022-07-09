package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
public class FilmController {
    private List<Film> films = new ArrayList<>();
    final static LocalDate BORN_FILMS = LocalDate.of(1895, Month.DECEMBER, 28);
    private int filmId = 0;

    @GetMapping(value = "/films")
    public List<Film> getFilms() {
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        save(film);
        log.info("Добавлен новый фильм '{}'",film.getName());
        return film;
    }

    private void validate(Film film) {
        if(film.getDescription().length() > 200) {
            log.error("Введено слишком длинное описание");
            throw new RuntimeException("invalid description");
        }
        if(film.getReleaseDate().isBefore(BORN_FILMS)) {
            log.error("Указана дата выпуска фильма раньше Дня рождения кино");
            throw new RuntimeException("invalid birthday");
        }
        if(film.getId() < 0) {
            log.error("ID не может быть меньше 0");
            throw new ValidationException("invalid Id");
        }
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        patch(film);
        log.info("Обновлены данные фильма '{}'", film.getName());
        return film;
    }

    private void patch(Film film) {
        boolean lost = true;
        for (Film oldFilm : films) {
            if (oldFilm.getId() == film.getId()) {
                lost = false;
                films.remove(oldFilm);
                films.add(film);
            }
        } if (lost) {
            films.add(film);
        }
    }

    private void save(Film film) {
        filmId++;
        film.setId(filmId);
        films.add(film);
    }
}

