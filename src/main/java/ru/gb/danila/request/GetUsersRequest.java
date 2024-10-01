package ru.gb.danila.request;

import java.io.Serializable;

public class GetUsersRequest implements Serializable {
    private String login;

    public GetUsersRequest(String login) {
        this.login = login;
    }

    public GetUsersRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
