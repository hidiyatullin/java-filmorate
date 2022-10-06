package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import javax.validation.Valid;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component
public class FilmDaoImpl implements FilmDao {

//    private Map<Long, Film> films = new HashMap<>();
//    private final static LocalDate BORN_FILMS = LocalDate.of(1895, Month.DECEMBER, 28);
//    private long filmId = 0;

    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Film> getById(long filmId) {
        String sql = "SELECT *" +
                "FROM films " +
                "LEFT JOIN film_genres on films.id = film_genres.film_id " +
                "LEFT JOIN rating on films.rating_id = rating.id " +
                "WHERE films.id = ?";
        List<Film> filmsList = jdbcTemplate.query(sql, FilmDaoImpl::makeFilm, filmId);
        if(!filmsList.isEmpty()) {
            Film film = filmsList.stream()
                    .findFirst()
                    .get();
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            throw new NotFoundException("Такой фильм не найден");
        }
    }

    @Override
    public Film create(@Valid @RequestBody Film film) {
        String sql = "INSERT INTO films(rating_id, name, description, releaseDate, duration) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // что такое keyHolder?
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"}); // что такое preparedStatement?
            stmt.setLong(1, film.getMpa().getId());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        addGenre(film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films " +
                "LEFT JOIN rating on films.rating_id = rating.id ";
        return jdbcTemplate.query(sql, FilmDaoImpl::makeFilm);
    }

    @Override
    public Optional<Film> update(Film film) {
        getById(film.getId());
        String sql = "UPDATE films " +
                "SET rating_id = ?, " +
                "name = ?, " +
                "description = ?, " +
                "releaseDate = ?, " +
                "duration = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                film.getMpa().getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        if (film.getGenres() != null) {
            deleteGenre(film.getId());
            for (Genre genre : film.getGenres()) {
                String sqlGenre = "MERGE INTO film_genres(genre_id, film_id) VALUES (?, ?)"; // ???
                jdbcTemplate.update(sqlGenre, genre.getId(), film.getId());
            }
        }
        log.info("Фильм {} обновлен. Новые данные {}", film.getName(), film);
        return getById(film.getId());
    }

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("films.id"))
                .mpa(new Mpa(
                        rs.getInt("rating.id"),
                        rs.getString("rating.name")))
                .name(rs.getString("films.name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .build();
    }

    private void addGenre(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sql = "INSERT INTO film_genres(genre_id, film_id) VALUES (?, ?)";
                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, Math.toIntExact(genre.getId()));
                        ps.setInt(2, Math.toIntExact(film.getId()));
                    }
                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                });
            }
        }
    }

    private void deleteGenre(long filmId) {
        String sqlDeleteGenre = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteGenre, filmId);
    }

//    @Override
//    public void delete(long id) {
//        String sql = "DELETE FROM films WHERE id = ?";
//        jdbcTemplate.update(sql, id);
//    } // удаление фильма не было

    @Override
    public List<Film> getPopular(int count) {
        String sql = "SELECT * FROM films " +
                "LEFT JOIN rating on films.rating_id = rating.id " +
                "ORDER BY films.rate DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, FilmDaoImpl::makeFilm, count);
    }
}
