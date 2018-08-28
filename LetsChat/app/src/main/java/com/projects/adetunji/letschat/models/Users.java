package com.projects.adetunji.letschat.models;

/**
 * Created by adetunji on 07/01/2018.
 */

public class Users {

    private String userId;
    private String emailId;
    private String lastMessage;
    private int notifCount;

    public String getUserId(){return userId;}
    public String getEmailId(){return emailId;}
    public String getLastMessage(){return lastMessage;}
    public int getNotifCount(){return notifCount;}

    public void setUserId(){this.userId = userId; }
    public void setEmailId(){this.emailId = emailId; }
    public void setLastMeassge(){this.lastMessage = lastMessage; }
    public void setNotifCount(int notifCount){this.notifCount = notifCount; }

}
