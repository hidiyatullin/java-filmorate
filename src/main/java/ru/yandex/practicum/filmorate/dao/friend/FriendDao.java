package ru.yandex.practicum.filmorate.dao.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {
    public List<User> getFriendsOfUser(long id);
    public List<User> findCommonFriends(long userId, long otherId);
    public void addFriend(long userId, long friendId);
    public void deleteFriend(long userId, long friendId);
}