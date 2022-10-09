package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

import static java.util.Collections.sort;

@Service
@RequiredArgsConstructor
public class FilmService {


    @Autowired
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private final UserDao userDao;
//    private final UserService userService;


    public List<Film> getFilms() {
        List<Film> filmsList = filmDao.getFilms();
        for (Film film : filmsList) {
            film.setGenres(filmDao.getFilmGenres(film.getId()));
        }
        return filmsList;
    }

    public Film create(Film film) {
        return filmDao.create(film);
    }

    public Optional<Film> update(Film film) {
        Optional<Film> updatedFilm = filmDao.update(film);
        updatedFilm.get().setGenres(filmDao.getFilmGenres(film.getId()));
        return updatedFilm;
    }

    public Optional<Film> getById(long id) {
        Optional<Film> film = filmDao.getById(id);
        film.get().setGenres(filmDao.getFilmGenres(id));
        return film;
    }

    public void addLike(long filmId, long userId) {
        likeDao.setLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        userDao.getUserById(userId);
        filmDao.getById(filmId);
        likeDao.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmDao.getPopular(count);
    }

}
