package com.projects.adetunji.letschat.models;

/**
 * Created by adetunji on 07/01/2018.
 */

public class UserEntity {
    public String Uid;
    public String Fullname;
    public String Username;
    public String Email;
    public String Phone;
    public String Birthday;
    public String Hobby;
    public String Relationship;
    public String Current_city;
    public String FirebaseToken;
    public String status;
    public long lastSeenDate;

    public UserEntity(){

    }

    public UserEntity(String uid, String email, String userName, String firebaseToken){
        this.Uid        = uid;
        this.Email      = email;
        this.Username   = userName;
        this.FirebaseToken = firebaseToken;
    }

    public UserEntity(String uId,
                      String email,
                      String userName,
                      String fullName,
                      String phone,
                      String birthday,
                      String hobby,
                      String Relationship,
                      String Current_city,
                      String status,
                      long lastSeenDate,
                      String firebaseToken ) {

        this.Uid            = uId;
        this.Email          = email;
        this.Fullname       = fullName;
        this.Username       = userName;
        this.Phone          = phone;
        this.Birthday       = birthday;
        this.Hobby          = hobby;
        this.Relationship   = Relationship;
        this.Current_city   = Current_city;
        this.status         = status;
        this.lastSeenDate   = lastSeenDate;
        if(!firebaseToken.isEmpty() || firebaseToken != "")
            this.FirebaseToken = firebaseToken;

    }

    public String mgetuId() {
        return Uid;
    }

    public String mgetEmail() {return Email;}

    public String mgetFullname() {
        return Fullname;
    }

    public String mgetUsername() {
        return Username;
    }

    public String mgetPhone() {
        return Phone;
    }

    public String mgetBirthday() {
        return Birthday;
    }

    public String mgetHobby() {
        return Hobby;
    }

    public String mgetRelationship() {
        return Relationship;
    }

    public String mgetCurrent_city() {
        return Current_city;
    }

    public String mgetStatus() {
        return status;
    }

    public String mgetFirebaseToken() {
        return FirebaseToken;
    }

    public long mgetLastSeenDate() {
        return lastSeenDate;
    }
}
