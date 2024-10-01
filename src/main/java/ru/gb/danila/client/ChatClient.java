package ru.gb.danila.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gb.danila.entity.User;
import ru.gb.danila.exceptions.ServerResponseException;
import ru.gb.danila.request.GetUsersRequest;
import ru.gb.danila.request.LoginRequest;
import ru.gb.danila.request.TypeRequest;
import ru.gb.danila.response.AbstractResponse;
import ru.gb.danila.response.GetUsersResponse;
import ru.gb.danila.response.DoneResponse;
import ru.gb.danila.request.SendMessageRequest;

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
    private boolean connected;

    public Client(User user) {
        this.user = user;
    }


    public void run() throws ConnectException, ServerResponseException {
        try(Socket socket = new Socket("localhost", 8888)){
            while (socket.isConnected()) {
                try (Scanner in = new Scanner(socket.getInputStream()); PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    while(socket.isConnected()){
                        try {
                            if(!connected) {
                                connectByLogin(user.getLogin(), in, out);
                                connected = true;
                            }
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

    private void listenCommand(Scanner in, PrintWriter out) throws ServerResponseException, JsonProcessingException {
        TypeCommand typeCommand = TypeCommand.valueOf(consoleScanner.nextLine());
        switch (typeCommand){
            case LOGIN -> {
                String login = prompt("input new login:", in, out);
                connectByLogin(login, in, out);
                user.setLogin(login);
            }

            case USERS -> {
                GetUsersRequest request = new GetUsersRequest(user.getLogin());
                GetUsersResponse response = serve(request, in, out, GetUsersResponse.class, TypeRequest.USER_LIST);
                System.out.println(response);

            }
            case MESSAGE -> {
                String message = prompt("message: ", in, out);
                SendMessageRequest request = new SendMessageRequest(user.getLogin(), message);
                DoneResponse response = serve(request,
                        in, out, DoneResponse.class, TypeRequest.SEND_MESSAGE);

                if(!response.isSuccessfully()){
                    throwResponseException(response.getErrorMessage());
                }

                System.out.println("send message successfully!");
            }
            case DISCONNECT -> {}
        }
    }

    private void throwResponseException(String errorMessage) throws ServerResponseException {
        throw new ServerResponseException(errorMessage);
    }

    private String prompt(String message, Scanner in, PrintWriter out) {
        System.out.print(message);
        return in.nextLine();
    }

    private void connectByLogin(String login, Scanner in, PrintWriter out) throws JsonProcessingException, ServerResponseException {
        LoginRequest loginRequest = new LoginRequest(user.getLogin());
        serve(loginRequest, in, out, DoneResponse.class, TypeRequest.LOGIN);
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

            if(!response.isSuccessfully()){
                throwResponseException("login connection with error");
            }

            return response;
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
