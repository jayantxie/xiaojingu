package com.jayantxie.pojo;

public class User {
    private String name;

    private String password;

    private String nickname;

    private String token;

    private String photo;

    private Byte workingState = 'a';

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public Byte getWorkingState() {
        return workingState;
    }

    public void setWorkingState(Byte workingState) {
        this.workingState = workingState;
    }
}