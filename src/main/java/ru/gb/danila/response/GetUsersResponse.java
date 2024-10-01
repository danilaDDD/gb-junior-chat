package ru.gb.danila.response;

import ru.gb.danila.entity.User;

import java.util.List;

public class GetUsersResponse extends AbstractResponse{
    private List<User> users;

    public GetUsersResponse(List<User> users) {
        super("", true);
        this.users = users;
    }

    public GetUsersResponse() {
        super("", true);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return users.toString();
    }
}
