package ru.gb.danila.request;

import java.io.Serializable;

public class SendMessageRequest implements Serializable {
    private String login;
    private String message;

    public SendMessageRequest(String login, String message) {
        this.login = login;
        this.message = message;
    }

    public SendMessageRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
