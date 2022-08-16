package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User get(long userId);
    User saveUser(User user);

    List<User> getUsers();

    void update(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);
}
