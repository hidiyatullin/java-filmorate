package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired // это остаётся при подключении БД?
    private UserService userService = new UserService();

    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    Optional<User> getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        validate(user);
        User saved = userService.create(user);
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

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsOfUser(@PathVariable long userId) {
        log.info("Получен перечень друзей пользователя " + userId);
        return userService.getFriendsOfUser(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        log.info("Пришёл запрос на общих друзей " + userId + " и " + otherId);
        return userService.findCommonFriends(userId, otherId);
    }

    public void validate(User user){
        if (user.getId() < 0) {
            log.error("ID не может быть меньше 0");
            throw new NotFoundException("invalid Id");
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
