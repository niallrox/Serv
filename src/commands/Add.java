package commands;


import Foundation.*;
import proga.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class Add extends AbstractCommand {
    private CollectionManager manager;
    private Data data;

    public Add(CollectionManager manager, Data data) {
        this.manager = manager;
        this.data = data;
    }

    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , SocketAddress inetSocketAddress, Route route, String login) throws InterruptedException {
        Runnable addElement = () -> {
            try {
                int id = data.getSQLId();
                data.addToSQL(route, login, id);
                route.setId(id);
                route.setLogin(login);
                manager.col.add(route);
                poolSend.submit(new Sender(datagramSocket, inetSocketAddress , "Элемент коллекции добавлен"));
            } catch (SQLException e) {
                e.printStackTrace();
                poolSend.submit(new Sender(datagramSocket, inetSocketAddress , "Ошибка при работе с БД (вероятно что-то с БД)"));
            } catch (NullPointerException e) {
                e.printStackTrace();
                poolSend.submit(new Sender(datagramSocket, inetSocketAddress , "Данные в скрипте введены не верно"));
            }
        };
        FTP.execute(addElement);
    }
}