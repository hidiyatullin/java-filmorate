package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class UserStorage {

    private HashMap<Long, User> users = new HashMap<>();
    private long userId = 0;

    public User get(long userId) {
        return users.get(userId);
    }

    public User saveUser(User user) {
        save(user);
        return user;
    }

    private void save(User user) {
        userId++;
        user.setId(userId);
        users.put(user.getId(), user);
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public void update(User user) {
        patch(user);
    }

    private void patch(User user) {
        boolean lost = true;
        for (User oldUser : users.values()) {
            if (oldUser.getId() == user.getId()) {
                lost = false;
                users.remove(oldUser.getId());
                users.put(user.getId(), user);
            }
        }
        if (lost) {
            log.error("Нельзя обновить пользователя, который ещё не сохранен");
            throw new ValidationException("Нет сохраненного пользователя с id " + user.getId());
        }
    }

    public void addFriend(User user, User friend) {
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
    }

    public void deleteFriend(User user, User friend) {
        user.getFriendIds().remove(friend.getId());
        friend.getFriendIds().remove(user.getId());
    }

    public Set<Long> getFriendsOfUser(long userId) {
        return users.get(userId).getFriendIds();
    }


}
