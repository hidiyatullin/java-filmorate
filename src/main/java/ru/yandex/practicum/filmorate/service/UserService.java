package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friend.FriendDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FriendDao friendDao;

    public Optional<User> getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    public User create(User user) {
        return userDao.create(user);
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public void update(User user) {
        userDao.update(user);
    }

    public void addFriend(long userId, long friendId) {
        if (userDao.getUserById(userId) == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        } else if (userDao.getUserById(friendId) == null) {
            throw new NotFoundException("User with id=" + friendId + " not found");
        } else {
            userDao.getUserById(userId);
            userDao.getUserById(friendId);
            friendDao.addFriend(userId, friendId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        if (userDao.getUserById(userId) == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        } else if (userDao.getUserById(friendId) == null) {
            throw new NotFoundException("User with id=" + friendId + " not found");
        } else {
            userDao.getUserById(userId);
            userDao.getUserById(friendId);
            friendDao.deleteFriend(userId, friendId);
        }
    }

    public List<User> getFriendsOfUser(long userId) {
        return friendDao.getFriendsOfUser(userId);
     }

    public List<User> findCommonFriends(long userId, long otherId) {
        return friendDao.findCommonFriends(userId, otherId);
    }
}
