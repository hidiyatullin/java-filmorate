package ru.yandex.practicum.filmorate.dao.like;

public interface LikeDao {
    public void setLike(long filmId, long userId);
    public void deleteLike(long filmId, long userId);
}