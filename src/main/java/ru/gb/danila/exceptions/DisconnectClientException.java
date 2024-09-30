package ru.gb.danila.exceptions;

import java.io.IOException;

public class DisconnectClientException extends IOException {
    public DisconnectClientException() {
        super("client disconnected");
    }
}
