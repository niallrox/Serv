package commands;

import Foundation.Route;
import proga.Data;
import proga.CollectionManager;
import proga.Sender;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.sql.SQLException;

import java.util.concurrent.ExecutorService;

public class RemoveHead extends AbstractCommand {
    private CollectionManager manager;
    private Data data;
    public RemoveHead(CollectionManager manager, Data data) {
        this.manager=manager;
        this.data = data;
    }

    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , SocketAddress inetSocketAddress, String login) throws NumberFormatException, InterruptedException {
        Runnable delete = () -> {
        if (manager.col.size() != 0) {
            Route a = manager.col.stream()
                    .findFirst()
                    .orElse(new Route());
            try {
                data.deleteById(a.getId(),login);
            } catch (SQLException e) {
                poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Ошибка при работе с БД (вероятно что-то с БД)"));
            }
            manager.col.remove(a);
            poolSend.submit(new Sender(datagramSocket , inetSocketAddress, a.toString()));

        } else poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Коллекция пуста"));
    };
        FTP.execute(delete);
    }
}
