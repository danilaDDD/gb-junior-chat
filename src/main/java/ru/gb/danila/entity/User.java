package ru.gb.danila.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String login;

    public User() {
    }

    public User(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return String.format("User{%s}", login);
    }
}
