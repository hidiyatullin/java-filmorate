package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
         private UserController userController = new UserController();

//    @Test
//    void correctUser() {
//        User userCorrect = new User(1, "1@mail.ru", "1", "Mike", LocalDate.of(1895, Month.DECEMBER, 28), null);
//        userController.saveUser(userCorrect);
//        Assertions.assertEquals(userController.getUsers().size(), 1);
//    }

    @Test
    void wrongBirthday() throws Exception {
        User userWrongBirthday = new User(1, "1@mail.ru", "1", "Mike", LocalDate.of(2200, Month.DECEMBER, 28), null);
        assertThrows(ValidationException.class, () -> userController.create(userWrongBirthday));
    }

//    @Test
//    void emptyName() {
//        User userWithoutName = new User(1, "1@mail.ru", "login", null, LocalDate.of(2000, Month.DECEMBER, 28), null);
//        Assertions.assertEquals(userController.saveUser(userWithoutName).getName(), "login");
//    }

    @Test
    void wrongLogin() throws Exception {
        User userWrongBirthday = new User(1, "1@mail.ru", " ", "Mike", LocalDate.of(2000, Month.DECEMBER, 28), null);
        assertThrows(ValidationException.class, () -> userController.create(userWrongBirthday));
    }

}