package ru.gb.danila.response;

import java.io.Serializable;

public  class AbstractResponse implements Serializable {
    private boolean successfully;
    private String errorMessage;

    public AbstractResponse(String errorMessage, boolean successfully) {
        this.errorMessage = errorMessage;
        this.successfully = successfully;
    }

    public AbstractResponse() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccessfully() {
        return successfully;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSuccessfully(boolean successfully) {
        this.successfully = successfully;
    }

    @Override
    public String toString() {
        return String.format("Response{%s, %s}", successfully, errorMessage);
    }
}
