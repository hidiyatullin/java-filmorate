package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;

    public User get(long userId) {
        final User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        return userStorage.get(userId);
    }

    public User saveUser(User user) {
        return userStorage.saveUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void update(User user) {
        userStorage.update(user);
    }

    public void addFriend(long userId, long friendId) {
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        } else if (userStorage.get(friendId) == null) {
            throw new NotFoundException("User with id=" + friendId + " not found");
        } else {
            User user = userStorage.get(userId);
            User friend = userStorage.get(friendId);
            userStorage.addFriend(user, friend);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        } else if (userStorage.get(friendId) == null) {
            throw new NotFoundException("User with id=" + friendId + " not found");
        } else {
            User user = userStorage.get(userId);
            User friend = userStorage.get(friendId);
            userStorage.deleteFriend(user, friend);
        }
    }

    public List<User> getFriendsOfUser(long userId) {
        List<User> list = new ArrayList<User>();
        for (long id : get(userId).getFriendIds()) {
            list.add(get(id));
        }
        return list;
     }

    public List<User> findCommonFriends(long userId, long otherId) {
        List<User> userFriends = getFriendsOfUser(userId);
        List<User> otherFriends = getFriendsOfUser(otherId);
        return userFriends.stream()
                .filter(user -> otherFriends.contains(user))
                .collect(Collectors.toList());
    }
}
