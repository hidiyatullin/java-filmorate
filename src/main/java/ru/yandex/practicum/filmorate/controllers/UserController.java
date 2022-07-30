package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService = new UserService();

    @GetMapping()
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable long userId) {
        return userService.get(userId);
    }

    @PostMapping()
    public User saveUser(@Valid @RequestBody User user) {
        validate(user);
        User saved = userService.saveUser(user);
        log.info("Добавлен новый пользователь '{}'", saved.getName());
        return saved;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        validate(user);
        userService.update(user);
        log.info("Обновлены данные пользователя '{}'", user.getName());
        return user;
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public Set<Long> getFriendsOfUser(@PathVariable long userId) {
        return userService.getFriendsOfUser(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{otherId}")
    public Set<Long> findCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return userService.findCommonFriends(userId, otherId);
    }

    public void validate(User user) throws NullPointerException{
        if (user.getId() < 0) {
            log.error("ID не может быть меньше 0");
            throw new ValidationException("invalid Id");
        }
        if (user.getLogin().equals(" ")) {
            log.error("Логин должен быть без пробелов");
            throw new ValidationException("invalid login");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.error("Поле Имя было пустым, заполнили его Логином");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ваша дата рождения ещё не наступила");
            throw new ValidationException("invalid birthday");
        }
    }
}
