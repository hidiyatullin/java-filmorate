package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private List<User> users = new ArrayList<>();
    private int userId = 0;

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        return users;
    }

    @PostMapping(value = "/users")
    User saveUser(@Valid @RequestBody User user) {
        validate(user);
        save(user);
        log.info("Добавлен новый пользователь '{}'",user.getName());
        return user;
    }

    private void save(User user) {
        userId++;
        user.setId(userId);
        users.add(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        validate(user);
        patch(user);
        return user;
    }

    private void patch(User user) {
        boolean lost = true;
        for (User oldUser : users) {
            if (oldUser.getId() == user.getId()) {
                lost = false;
                users.remove(oldUser);
                users.add(user);
            }
        } if (lost) {
            users.add(user);
        }
    }

    void validate(User user) {
        if(user.getId() < 0) {
            log.error("ID не может быть меньше 0");
            throw new ValidationException("invalid Id");
        }
        if(user.getLogin().equals(" ")) {
            log.error("Логин должен быть без пробелов");
            throw new ValidationException("invalid login");
        }
        if(user.getName().isEmpty() || user.getName() == null) {
            log.error("Поле Имя было пустым, заполнили его Логином");
            user.setName(user.getLogin());
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ваша дата рождения ещё не наступила");
            throw new ValidationException("invalid birthday");
        }
    }




}
