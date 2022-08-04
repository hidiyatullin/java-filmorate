package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User saveUser(User user);

    void update(User user);
}
