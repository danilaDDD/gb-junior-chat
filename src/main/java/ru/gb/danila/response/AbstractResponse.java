package ru.gb.danila.response;

public  class AbstractResponse {
    private boolean successfully;
    private String errorMessage;

    public AbstractResponse(String errorMessage, boolean successfully) {
        this.errorMessage = errorMessage;
        this.successfully = successfully;
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
}
