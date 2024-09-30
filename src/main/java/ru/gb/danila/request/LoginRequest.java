package ru.gb.danila.request;

public class LoginRequest extends AbstractRequest{
    private String login;

    public LoginRequest(String login) {
        super(TypeRequest.LOGIN);
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
