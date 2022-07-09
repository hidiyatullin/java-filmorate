package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.util.Assert;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
         private UserController userController = new UserController();

    @Test
    void correctUser() {
        User userCorrect = new User(1, "1@mail.ru", "1", "Mike", LocalDate.of(1895, Month.DECEMBER, 28));
        userController.saveUser(userCorrect);
        Assertions.assertEquals(userController.getUsers().size(), 1);
    }

    @Test
    void wrongBirthday() throws Exception {
        User userWrongBirthday = new User(1, "1@mail.ru", "1", "Mike", LocalDate.of(2200, Month.DECEMBER, 28));
        assertThrows(ValidationException.class, () -> userController.saveUser(userWrongBirthday));
    }

//    @Test
//    void emptyName() {
//        User userWithoutName = new User(1, "1@mail.ru", "login", null, LocalDate.of(2200, Month.DECEMBER, 28));
//        userController.validate(userWithoutName);
//        Assertions.assertEquals(userController.getUsers().get(0).getName(), "login");
//    }

}