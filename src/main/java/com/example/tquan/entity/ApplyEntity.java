package com.example.tquan.entity;

public class ApplyEntity {
    private String userId;
    private String login_name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    @Override
    public String toString() {
        return "applyEntity{" +
                "userId='" + userId + '\'' +
                ", login_name='" + login_name + '\'' +
                '}';
    }
}
