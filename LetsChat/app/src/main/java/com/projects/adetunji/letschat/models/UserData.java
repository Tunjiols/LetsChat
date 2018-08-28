package com.projects.adetunji.letschat.models;

/**
 * Created by adetunji on 01/03/2018.
 */

public class UserData {
    public String Email;
    public String Username;
    public String Uid;
    public String FirebaseToken;

    public UserData(String email, String username, String Uid, String firebaseToken) {
        this.Email = email;
        this.Username = username;
        this.Uid = Uid;
        this.FirebaseToken = firebaseToken;
    }

    public String getEmail() {
        return Email;
    }

    public String getUsername() {
        return Username;
    }

	 public String getUid() {
        return Uid;
    }

    public String getFirebaseToken() {
        return FirebaseToken;
    }
}
