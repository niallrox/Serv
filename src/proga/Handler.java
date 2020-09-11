package proga;


import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Handler {
    public ExecutorService pool = Executors.newFixedThreadPool(2);

    public void handler(Command command, CollectionManager manager, Data data, ExecutorService poolSend, DatagramSocket datagramSocket, InetAddress inetAddress) {
        try {
            if (command.getName().equals("reg")) {
                Sender s = new Sender(datagramSocket, inetAddress, data.registration(command));
                poolSend.submit(s);
            } else if (command.getName().equals("sign")) {
                if (data.authorization(command)) {
                    System.out.println("Пользователь с логином " + command.getLogin() + " успешно авторизован.");
                    poolSend.submit(new Sender(datagramSocket, inetAddress, "Авторизация прошла успешно"));
                } else {
                    System.out.println("Пользователь ввел не верный пароль");
                    poolSend.submit(new Sender(datagramSocket, inetAddress, "Логин или пароль введены неверно"));
                }
            } else {
                if (data.authorization(command)) {
                    switch (command.getName()) {
                        case "clear": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress , command.getLogin());
                        }
                        break;
                        case "show":
                        case "info":
                        case "help":
                        case "print_field_ascending_students_count":
                        case "print_field_descending_form_of_education": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress);
                        }
                        break;
                        case "remove_greater":
                        case "remove_by_id":
                        case "remove_any_by_students_count":
                        case "execute_script": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress, command.getArgs(), command.getLogin());
                        }
                        break;
                        case "add_if_max":
                        case "add_if_min":
                        case "add": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress, command.getRoute(), command.getLogin());
                        }
                        break;
                        case "update": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress,command.getArgs(), command.getRoute(), command.getLogin());
                        }
                    }
                    System.out.println("Обработана команда " + command.getName());
                } else {
                    System.out.println("Кто-то обошел защиту сервера");
                    poolSend.submit(new Sender(datagramSocket, inetAddress, "О вы юный хакер"));
                }
            }
        } catch (NoSuchAlgorithmException | ExecutionException | InterruptedException | UnsupportedEncodingException | SQLException e) {
            // Все под контролем
        }
    }
}