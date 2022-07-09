package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private HashMap<Long, User> users = new HashMap<>();
    private long userId = 0;

    @GetMapping("/users")
    public ArrayList<User> getUsers() {
        return new ArrayList(users.values());
    }

    @PostMapping("/users")
    public User saveUser(@Valid @RequestBody User user) {
        validate(user);
        save(user);
        log.info("Добавлен новый пользователь '{}'", user.getName());
        return user;
    }

    private void save(User user) {
        userId++;
        user.setId(userId);
        validate(user);
        users.put(user.getId(), user);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        validate(user);
        patch(user);
        log.info("Обновлены данные пользователя '{}'", user.getName());
        return user;
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
