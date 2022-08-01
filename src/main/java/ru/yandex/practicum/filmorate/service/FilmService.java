package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

import java.util.ArrayList;
import java.util.Comparator;

import static java.util.Collections.sort;

@Service
public class FilmService {

    @Autowired
    FilmStorage filmStorage;

    @Autowired
    UserStorage userStorage;

    public Film get(long filmId) {
        final Film film = filmStorage.get(filmId);
        if (film == null) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        }
        return filmStorage.get(filmId);
    }

    public Film save(Film film) {
        return filmStorage.saveFilm(film);
    }

    public ArrayList<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void update(Film film) {
        filmStorage.update(film);
    }

    public void addLike(long filmId, long userId) {
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        } else if (filmStorage.get(filmId) == null) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        } else {
            Film film = filmStorage.get(filmId);
            User user = userStorage.get(userId);
            filmStorage.addLike(film, user);
        }
    }

    public void deleteLike(long filmId, long userId) {
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        } else if (filmStorage.get(filmId) == null) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        } else {
            User user = userStorage.get(userId);
            Film film = filmStorage.get(filmId);
            filmStorage.deleteLike(film, user);
        }
    }

    public List<Film> getFilmLikes(int count) {
        int countFilms = 9;
        if (count > 0) {
            countFilms = count - 1;
        }
        List<Film> films = filmStorage.getFilms();
        List<Film> sortedFilms = new ArrayList<>();
        Collections.sort(films, new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getUserIds().size() - o1.getUserIds().size();
            }
        });
        if (countFilms > films.size()) {
            countFilms = films.size() - 1;
        }
        if (countFilms == 0) {
            sortedFilms.add(films.get(0));
        } else {
            for (int i = 0; i <= countFilms; i++) {
                sortedFilms.add(films.get(i));
            }
        }
        return sortedFilms;
    }

}
