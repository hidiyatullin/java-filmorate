package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    public Optional<Genre> getById(long id) {
        return genreDao.getById(id);
    }
}