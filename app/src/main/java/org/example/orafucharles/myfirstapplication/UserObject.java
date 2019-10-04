package org.example.orafucharles.myfirstapplication;

public class UserObject {

    private String mUsername;
    private String mEmail;
    private int id;



    public UserObject(int id, String username){
        this.mUsername = username;
        this.id = id;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmEmail() {
        return mEmail;
    }

    public int getId() {
        return id;
    }
}
