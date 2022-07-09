package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController = new FilmController();

    @Test
    void emptyName() throws Exception{
        Film filmWithoutName = new Film(1, " ", "melodrama", LocalDate.of(1997, Month.DECEMBER, 28), 220);
        assertThrows(ValidationException.class, () -> filmController.create(filmWithoutName));
    }

    @Test
    void longDescription() throws Exception{
        Film filmWithLongDescription = new Film(1, "Titanic", "«Тита́ник» (англ. Titanic) — американский " +
                "фильм-катастрофа 1997 года, снятый режиссёром Джеймсом Кэмероном, в котором показана гибель " +
                "легендарного лайнера «Титаник». Герои фильма, будучи представителями различных социальных слоёв, " +
                "влюбились друг в друга на борту лайнера, совершавшего свой первый и последний рейс через Атлантический " +
                "океан в 1912 году. Главные роли исполнили Леонардо Ди Каприо и Кейт Уинслет.",
                LocalDate.of(1997, Month.DECEMBER, 28), 220);
        assertThrows(ValidationException.class, () -> filmController.create(filmWithLongDescription));
    }

    @Test
    void wrongRelease() throws Exception {
        Film filmWithWrongRelease = new Film(1, "Titanic", "melodrama", LocalDate.of(1800, Month.DECEMBER, 28), 220);
        assertThrows(ValidationException.class, () -> filmController.create(filmWithWrongRelease));
    }

    @Test
    void positiveDuration() throws Exception {
        Film filmWithWrongRelease = new Film(1, "Titanic", "melodrama", LocalDate.of(1997, Month.DECEMBER, 28), -120);
        assertThrows(ValidationException.class, () -> filmController.create(filmWithWrongRelease));
    }

}