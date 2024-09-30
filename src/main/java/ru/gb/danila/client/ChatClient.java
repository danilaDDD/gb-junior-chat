package ru.gb.danila.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gb.danila.entity.User;
import ru.gb.danila.exceptions.ServerResponseException;
import ru.gb.danila.request.LoginRequest;
import ru.gb.danila.request.TypeRequest;
import ru.gb.danila.response.AbstractResponse;
import ru.gb.danila.response.LoginResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ChatClient {
    private static final Scanner consoleScanner = new Scanner(System.in);

    public static void main(String[] args) {
        User user = new User(prompt("You must input login: "));

        while(true) {
            try {
                new Client(user).run();

            } catch (ConnectException e) {
                System.err.println("server not found");
            }catch (ServerResponseException e){
                System.err.println(e.getMessage());
            }
            System.out.println("try connect again");
        }
    }

    private static String prompt(String message) {
        System.out.print(message);
        return consoleScanner.nextLine();
    }
}

class Client{
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Scanner consoleScanner = new Scanner(System.in);

    private final User user;

    public Client(User user) {
        this.user = user;
    }


    public void run() throws ConnectException, ServerResponseException {
        try(Socket socket = new Socket("localhost", 8888)){
            while (socket.isConnected()) {
                try (Scanner in = new Scanner(socket.getInputStream()); PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    while(socket.isConnected()){
                        try {
                            connectByLogin(user.getLogin(), in, out);
                            System.out.printf("available commands: %s%n", Arrays.toString(TypeCommand.values()));
                            listenCommand(in, out);
                        }catch (IllegalArgumentException e){
                            System.out.println("this command not found");
                        } catch (IOException | ServerResponseException e) {
                            System.err.println(e.getMessage());
                        }
                        System.out.println("try input command again");
                    }
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());

        }
    }

    private void listenCommand(Scanner in, PrintWriter out) {
        TypeCommand typeCommand = TypeCommand.valueOf(in.nextLine());
        switch (typeCommand){
            case LOGIN -> {}
            case USERS -> {}
            case MESSAGE -> {}
            case DISCONNECT -> {}
        }
    }

    private void connectByLogin(String login, Scanner in, PrintWriter out) throws JsonProcessingException, ServerResponseException {
        LoginRequest loginRequest = new LoginRequest(user.getLogin());
        serve(loginRequest, in, out, LoginResponse.class, TypeRequest.LOGIN);
    }

    private <RS extends AbstractResponse, RQ> RS serve(RQ request,
                                                                               Scanner in, PrintWriter out,
                                                                               Class<RS> responseClass, TypeRequest typeRequest) throws ServerResponseException {
        // если при данной ошибки произошло исключение - это ошибка программиста)
        try {
            out.println(typeRequest);
            out.println(mapper.writeValueAsString(request));
            String s = in.nextLine();
            RS response = mapper.readValue(s, responseClass);
            System.out.println(response);
            if(!response.isSuccessfully()){
                throw new ServerResponseException("login connection with error");
            }

            return response;
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
