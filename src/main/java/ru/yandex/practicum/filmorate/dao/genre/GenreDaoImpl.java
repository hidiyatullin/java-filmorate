package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", GenreDaoImpl::makeGenre);
    }

    @Override
    public Optional<Genre> getById(long id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genreList = jdbcTemplate.query(sql, GenreDaoImpl::makeGenre, id);
        if(!genreList.isEmpty()) {
            Genre genre = genreList.stream()
                    .findFirst()
                    .get();
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new NotFoundException("Такой жанр не найден");
        }
    }

    @Override
    public List<Genre> getFilmGenres(long filmId) {
        String sql = "SELECT * FROM film_genres WHERE film_id = ?";
        List<Long> filmsGenreList = jdbcTemplate.query(sql, GenreDaoImpl::makeFilmGenres, filmId);
        List<Genre> genresOfFilm = new ArrayList<>();
        for (long genreId : filmsGenreList) {
            genresOfFilm.add(getById(genreId).get());
        }
        return genresOfFilm;
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("id"),
                rs.getString("name"));
    }

    static long makeFilmGenres(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("genre_id");
    }
}