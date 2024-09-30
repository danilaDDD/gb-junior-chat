package ru.gb.danila.response;

public class LoginResponse extends AbstractResponse{
    public LoginResponse(String errorMessage, boolean successfully) {
        super(errorMessage, successfully);
    }

    public LoginResponse() {
        super("", true);
    }
}
