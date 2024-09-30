package ru.gb.danila.request;

public abstract class AbstractRequest {
    private TypeRequest type;

    public AbstractRequest(TypeRequest type) {
        this.type = type;
    }

    public TypeRequest getType() {
        return type;
    }
}
