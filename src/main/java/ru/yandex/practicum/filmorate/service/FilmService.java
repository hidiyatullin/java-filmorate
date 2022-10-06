package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

import static java.util.Collections.sort;

@Service
@RequiredArgsConstructor
public class FilmService {


//    @Autowired
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private final UserService userService;


    public List<Film> getFilms() {
        List<Film> filmsList = filmDao.getFilms();
        for (Film film : filmsList) {
            film.setGenres(genreDao.getFilmGenres(film.getId()));
        }
        return filmsList;
    }

    public Film create(Film film) {
        return filmDao.create(film);
    }

    public Optional<Film> update(Film film) {
        /*Film updatedFilm = filmDao.update(film);*/
        Optional<Film> updatedFilm = filmDao.update(film);
        updatedFilm.get().setGenres(genreDao.getFilmGenres(film.getId()));
        return updatedFilm;
    }

    public Optional<Film> getById(long id) {
        /*Film film = filmDao.getById(id);*/
        Optional<Film> film = filmDao.getById(id);
        film.get().setGenres(genreDao.getFilmGenres(id));
        return film;
    }

    public void addLike(long filmId, long userId) {
        likeDao.setLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        //userService.getById(userId); // не понимаю какую функцию выполняет. Проверка наличия данных?
        likeDao.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmDao.getPopular(count);
    }

//    public void delete(long id) { // метода удаления не было изначально
//        filmDao.delete(id);
//    }

}
