package com.gnoulel.birthdayforfriends.repository.customquery;

public class FriendCustomQuery {
    public static final String IS_EMAIL_EXISTED_BELONG_TO_USER = "SELECT " +
            " IF(COUNT(f.id) > 0, 'true', 'false') " +
            " FROM `friend` f INNER JOIN `user` u ON f.user_id = u.id " +
            " WHERE u.email = :username AND f.email = :email ";

    public static final String IS_PHONE_EXISTED_BELONG_TO_USER = "SELECT " +
            " IF(COUNT(f.id) > 0, 'true', 'false') " +
            " FROM `friend` f INNER JOIN `user` u ON f.user_id = u.id " +
            " WHERE u.email = :username AND f.phone = :phone ";

    public static final String GET_FRIENDS_BELONG_TO_USER = "" +
            " SELECT f " +
            " FROM Friend f INNER JOIN f.user u " +
            " WHERE u.email = :username ";
}
