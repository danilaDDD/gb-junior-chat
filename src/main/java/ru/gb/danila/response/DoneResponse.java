package ru.gb.danila.response;

public class DoneResponse extends AbstractResponse{
    public DoneResponse(String errorMessage, boolean successfully) {
        super(errorMessage, successfully);
    }

    public DoneResponse() {
        super("", true);
    }
}
