package ru.gb.danila.exceptions;

public class ServerNotFoundException extends RuntimeException{
    public ServerNotFoundException() {
        super("server not found");
    }
}
