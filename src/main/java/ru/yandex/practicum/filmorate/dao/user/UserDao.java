package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(long userId);
    User create(User user);

    List<User> getUsers();

    User update(User user);

    /*void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);*/ // переехали в FriendDao
}
