package ru.gb.danila.request;

public class LoginRequest{
    private String login;

    public LoginRequest(String login) {
        this.login = login;
    }

    public LoginRequest() {
    }

    public String getLogin() {
        return login;
    }
}
