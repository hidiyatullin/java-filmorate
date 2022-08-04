package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.dao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import static java.util.Collections.sort;

@Service
public class FilmService {

    @Autowired
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    public Film get(long filmId) {
        final Film film = inMemoryFilmStorage.get(filmId);
        if (film == null) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        }
        return inMemoryFilmStorage.get(filmId);
    }

    public Film save(Film film) {
        return inMemoryFilmStorage.saveFilm(film);
    }

    public ArrayList<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
    }

    public void update(Film film) {
        inMemoryFilmStorage.update(film);
    }

    public void addLike(long filmId, long userId) {
        if (inMemoryUserStorage.get(userId) == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        } else if (inMemoryFilmStorage.get(filmId) == null) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        } else {
            Film film = inMemoryFilmStorage.get(filmId);
            User user = inMemoryUserStorage.get(userId);
            inMemoryFilmStorage.addLike(film, user);
        }
    }

    public void deleteLike(long filmId, long userId) {
        if (inMemoryUserStorage.get(userId) == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        } else if (inMemoryFilmStorage.get(filmId) == null) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        } else {
            User user = inMemoryUserStorage.get(userId);
            Film film = inMemoryFilmStorage.get(filmId);
            inMemoryFilmStorage.deleteLike(film, user);
        }
    }

    Comparator<Film> comparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return o2.getUserIds().size() - o1.getUserIds().size();
        }
    };

    public List<Film> getFilmLikes(int count) {
        List<Film> films = inMemoryFilmStorage.getFilms();
        Collections.sort(films, comparator);
        return films.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

}
