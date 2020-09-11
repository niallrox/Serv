package commands;

import proga.Data;
import proga.CollectionManager;
import proga.Sender;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class RemoveId extends AbstractCommand {
    private CollectionManager manager;
    private Data data;
    private String answer;

    public RemoveId(CollectionManager manager, Data data) {
        this.manager = manager;
        this.data = data;
    }


    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetAddress inetSocketAddress, String str, String login) throws NumberFormatException, InterruptedException {
        Runnable delete = () -> {
            if (!(manager.col.size() == 0)) {
                Long id = Long.parseLong(str);
                try {
                    data.deleteById(id, login);
                } catch (SQLException e) {
                    poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Ошибка при работе с БД (вероятно что-то с БД)"));
                }
                if (manager.col.removeIf(col -> col.getId() == id && col.getLogin().equals(login))) {
                    poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Элемент удален"));
                } else
                    poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Нет элемента с таким id или пользователь не имеет доступа к этому элементу"));
            } else {
                poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Коллекция пуста"));
            }
        };
        FTP.execute(delete);
    }
}