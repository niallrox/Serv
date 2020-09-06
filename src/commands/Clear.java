package commands;

import proga.Data;
import proga.CollectionManager;
import proga.ServerSender;


import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class Clear extends AbstractCommand {
    private CollectionManager manager;
    private Data data;
    private String answer;

    public Clear(CollectionManager manager, Data data) {
        this.manager = manager;
        this.data = data;
    }

    /**
     * Метод очищает коллекцию
     *
     * @return
     */
    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress, String login) throws InterruptedException {
        Runnable clear = () -> {
            try {
                data.clearSQL(login);
                if (manager.col.removeIf(col -> col.getLogin().equals(login))) {
                    poolSend.submit(new ServerSender(datagramSocket, inetSocketAddress ,  "Коллекция очищена. Удалены все принадлежащие вам элементы"));
                } else {
                    poolSend.submit(new ServerSender(datagramSocket, inetSocketAddress, "В коллекции нет элементов принадлежащих пользователю"));
                }
            } catch (SQLException e) {
                poolSend.submit(new ServerSender(datagramSocket, inetSocketAddress, "Ошибка при работе с БД (вероятно что-то с БД)"));
            }
        };
        FTP.execute(clear);
    }
}