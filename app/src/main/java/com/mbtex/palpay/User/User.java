package com.mbtex.palpay.User;

/**
 * User Singleton
 */
public class User {

    private String auth_token;
    private String username;
    private String email;
    private String display_picture_url ;
    private static User active_user;


    private User(String username, String token, String email, String display_picture_url)
    {
        this.auth_token = token;
        this.username = username;
        this.display_picture_url = display_picture_url;
        this.email = email;
    }

    static User getUser(String username, String token, String display_picture_url, String email)
    {
        try {
            active_user = new User(username, token, display_picture_url, email);
        } catch(Exception e) {
            throw new RuntimeException("Create User Excpetion");
        }

        return active_user;
    }

    static User getUser() {
        return active_user;
    }

    public String getAuthToken() {
        return auth_token;
    }

    public String getUserName() {
        return username;
    }

    public String getEmail()
    {
        return this.email;
    }
}
