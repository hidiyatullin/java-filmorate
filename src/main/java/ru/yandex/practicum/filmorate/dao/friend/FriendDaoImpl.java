package ru.yandex.practicum.filmorate.dao.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendDaoImpl implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    // Получение списка друзей
    @Override
    public List<User> getFriendsOfUser(long id) {
        String sql = "SELECT * FROM users, friends " +
                "WHERE users.id = friends.user2_id " +
                "AND friends.user1_id = ?";
        return jdbcTemplate.query(sql, FriendDaoImpl::makeUser, id);
    }

    // Получение общих друзей
    @Override
    public List<User> findCommonFriends(long userId, long otherId) {
        String sql = "SELECT * " +
                "FROM friends " +
                "LEFT JOIN users ON users.ID=FRIENDS.user2_id " +
                "WHERE user1_id = ?" +
                "AND user2_id IN (SELECT user2_id FROM friends WHERE user1_id = ?)";
        return jdbcTemplate.query(sql, FriendDaoImpl::makeUser, userId, otherId);
    }

    // Добавление в друзья
    @Override
    public void addFriend(long userId, long friendId) {
        String sql = "INSERT INTO friends(user1_id, user2_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    // Удаление из друзей
    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM friends WHERE user1_id = ? AND user2_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException { // проверить реализацию, не полный запрос по конструктору
        return new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}