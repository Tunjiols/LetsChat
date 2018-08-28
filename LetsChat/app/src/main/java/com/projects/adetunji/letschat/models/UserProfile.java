package com.projects.adetunji.letschat.models;

/**
 * Created by adetunji on 20/01/2018.
 */

public class UserProfile {
    private String header;

    private String profileContent;

    public UserProfile(String header, String profileContent) {
        this.header = header;
        this.profileContent = profileContent;
    }

    public String getHeader() {
        return header;
    }

    public String getProfileContent() {
        return profileContent;
    }
}
