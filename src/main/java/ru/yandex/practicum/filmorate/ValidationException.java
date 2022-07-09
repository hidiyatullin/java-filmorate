package ru.yandex.practicum.filmorate;

public class ValidationException extends RuntimeException{
    public ValidationException(final String message) {
        super(message);
    }
}
