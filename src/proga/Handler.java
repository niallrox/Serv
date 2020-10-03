package proga;


import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Handler {
    public ExecutorService pool = Executors.newFixedThreadPool(2);

    public void handler(Command command, CollectionManager manager, Data data, ExecutorService poolSend, DatagramSocket datagramSocket, SocketAddress inetAddress) {
        try {
            if (command.getName().equals("reg")) {
                String answer = data.registration(command);
                Sender s = new Sender(datagramSocket, inetAddress, answer);
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
                        case "remove_head":
                        case "clear": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress , command.getLogin());
                        }
                        break;
                        case "show":
                        case "info":
                        case "help":
                        case "print_field_ascending_distance":
                        case "max_by_from":
                        case "min_by_distance": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress);
                        }
                        break;
                        case "remove_by_id":
                        case "execute_script": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress, command.getArgs(), command.getLogin());
                        }
                        break;
                        case "remove_lower":
                        case "add_if_max":
                        case "add": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress, command.getRoute(), command.getLogin());
                        }
                        break;
                        case "update": {
                            manager.commandMap.get(command.getName()).executeCommand(pool, poolSend, datagramSocket, inetAddress,command.getArgs(), command.getRoute(), command.getLogin());
                        }
                    }
                    if (!command.getName().equals("show")){
                    System.out.println("Обработана команда " + command.getName());}
                } else {
                    System.out.println("Кто-то обошел защиту сервера");
                    poolSend.submit(new Sender(datagramSocket, inetAddress, "V-vendetta"));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Неверный ключ шифрования");
        } catch (ExecutionException e){
            System.out.println("Ошибка исполнения");
        } catch (InterruptedException e){
            System.out.println("Ошибка работы с потоками");
        } catch (UnsupportedEncodingException e){
            System.out.println("Неверная кодировка");
        } catch (SQLException e){
            System.out.println("Ошибка при работе с БД");
        }
    }
}