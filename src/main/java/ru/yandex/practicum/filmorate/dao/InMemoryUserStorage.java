package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private long userId = 0;

    @Override
    public User get(long userId) {
        return users.get(userId);
    }

    @Override
    public User saveUser(User user) {
        save(user);
        return user;
    }

    private void save(User user) {
        userId++;
        user.setId(userId);
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
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
            throw new NotFoundException("Нет сохраненного пользователя с id " + user.getId());
        }
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriendIds().add(friend.getId());
        log.info("Добавлен друг " + friend.getId() + "пользователю " + user.getId());
        friend.getFriendIds().add(user.getId());
        log.info("Добавлен пользователь " + user.getId() + "другу " + friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriendIds().remove(friend.getId());
        log.info("Удален друг " + friend.getId() + "пользователя" + user.getId());
        friend.getFriendIds().remove(user.getId());
        log.info("Удален пользователь " + user.getId() + " у друга " + friend.getId());
    }

}
