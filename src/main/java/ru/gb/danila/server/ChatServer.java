package ru.gb.danila.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gb.danila.entity.User;
import ru.gb.danila.exceptions.BadRequestException;
import ru.gb.danila.request.AbstractRequest;
import ru.gb.danila.request.LoginRequest;
import ru.gb.danila.response.BadRequestResponse;
import ru.gb.danila.response.LoginResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 0. Осознать код, который написали на уроке
 * 1.1 Отправить сообщение всем пользователям (кроме себя)
 * 1.2 UsersRequest - получить список всех пользователей, которые в данный момент в чате
 * DisconnectRequest -клиент попвещает сервер что он отключен
 * При отключении клиента оповесттить всех остальных
 *
 * Можно сделать только один пункт из 1.1 - 1.3
 */

public class ChatServer {

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8888)){
            System.out.println("Server run");

            while(true) {
                Socket accept = server.accept();
                new Thread(new ClientHandler(accept)).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class ClientHandler implements Runnable{
    private static Map<String, User> userOnlineMap = new ConcurrentHashMap<>();
    private static ObjectMapper mapper = new ObjectMapper();

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(socket.getInputStream()); PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            while (true) {
                listenClient(in, out);
            }

        } catch (IOException e) {
           e.printStackTrace();
           //doClose();
        }
    }

    private void doClose() {
        try {
            socket.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void listenClient(Scanner in, PrintWriter out) {
        try{
            String jsonRequest = in.nextLine();
            AbstractRequest abstractRequest = mapper.readValue(jsonRequest, AbstractRequest.class);

            switch (abstractRequest.getType()){
                case LOGIN -> {
                    LoginRequest request = mapper.readValue(jsonRequest, LoginRequest.class);
                    if(userOnlineMap.get(request.getLogin()) != null){
                        throw new BadRequestException("user already online");
                    }
                    userOnlineMap.put(request.getLogin(), new User(request.getLogin()));
                    out.println(mapper.writeValueAsString(new LoginResponse()));
                }
                default -> {
                    throw new BadRequestException("not correct request type");
                }
            }
        } catch (BadRequestException | JsonProcessingException e) {
            onBadRequest(e.getMessage(), in, out);
        }

    }

    private void onBadRequest(String message, Scanner in, PrintWriter out) {
        try {
            out.println(mapper.writeValueAsString(new BadRequestResponse()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}