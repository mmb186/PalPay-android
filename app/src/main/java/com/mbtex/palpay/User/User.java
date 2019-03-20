package com.mbtex.palpay.User;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

/**
 * User Singleton
 */
public class User implements Parcelable {

    private String _auth_token;
    private String _username;
    private String _email;
    private String display_picture_url ;
    private static User active_user;


    private User(String username, String token, String email, String display_picture_url)
    {
        this._auth_token = token;
        this._username = username;
        this.display_picture_url = display_picture_url;
        this._email = email;
    }

    protected User(Parcel in) {
        _auth_token = in.readString();
        _username = in.readString();
        _email = in.readString();
        display_picture_url = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User getUser(String username, String token, String email, String display_picture_url)
    {
        try {
            active_user = new User(username, token, email,  display_picture_url);
        } catch(Exception e) {
            throw new RuntimeException("Create User Exception");
        }

        return active_user;
    }

    static User getUser() {
        return active_user;
    }

    public String getAuthToken() {
        return _auth_token;
    }

    public String getUserName() {
        return _username;
    }

    public String getEmail()
    {
        return this._email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_auth_token);
        dest.writeString(_username);
        dest.writeString(_email);
        dest.writeString(display_picture_url);
    }

    public Intent createIntentAndAddSelf(AppCompatActivity fromActivity, Class targetActivity) {
        Intent intent = new Intent(fromActivity, targetActivity);
        intent.putExtra("current_user", this);
        return intent;
    }
}
