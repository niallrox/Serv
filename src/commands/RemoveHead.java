package commands;

import Foundation.Route;
import proga.Data;
import proga.CollectionManager;
import proga.ServerSender;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
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
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress, String str, String login) throws NumberFormatException, InterruptedException {
        Runnable delete = () -> {
        if (manager.col.size() != 0) {
            Route a = manager.col.stream()
                    .findFirst()
                    .orElse(new Route());
            try {
                data.deleteById(a.getId(),login);
            } catch (SQLException e) {
                poolSend.submit(new ServerSender(datagramSocket , inetSocketAddress, "Ошибка при работе с БД (вероятно что-то с БД)"));
            }
            manager.col.remove(a);
            poolSend.submit(new ServerSender(datagramSocket , inetSocketAddress, a.toString()));

        } else poolSend.submit(new ServerSender(datagramSocket , inetSocketAddress, "Коллекция пуста"));
    };
        FTP.execute(delete);
    }
}