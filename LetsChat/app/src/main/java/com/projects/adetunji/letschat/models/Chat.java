package com.projects.adetunji.letschat.models;

/**
 * Created by adetunji on 07/01/2018.
 */

public class Chat {
    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public boolean isRead;
    public long timestamp;
    public int LIKE_STATUS  = 0;


    public Chat(){}

    public Chat(String sender,
                String receiver,
                String senderUid,
                String receiverUid,
                String meaasge,
                long timestamp,
                boolean isRead,
                int LIKE_STATUS){

        this.sender         = sender;
        this.receiver       = receiver;
        this.senderUid      = senderUid;
        this.receiverUid    = receiverUid;
        this.message        = meaasge;
        this.timestamp      = timestamp;
        this.isRead         = isRead;
        this.LIKE_STATUS    = LIKE_STATUS;
    }


}
