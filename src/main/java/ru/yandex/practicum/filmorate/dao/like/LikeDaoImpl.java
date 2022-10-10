package ru.yandex.practicum.filmorate.dao.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    // Ставим лайки
    @Override
    public void setLike(long filmId, long userId) {
        String sql = "INSERT INTO film_likes(user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
        updateRate(filmId);
    }

    // Удаляем лайк
    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
        updateRate(filmId);
    }

    private void updateRate(long filmId) {
        String sqlQuery = "UPDATE films f " +
                "set f.rate = (SELECT count(l.user_id) FROM film_likes l WHERE l.film_id = f.id) " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}