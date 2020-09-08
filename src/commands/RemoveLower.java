package commands;


import Foundation.Route;
import proga.Data;
import proga.CollectionManager;
import proga.Sender;


import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;


public class RemoveLower extends AbstractCommand {
    private CollectionManager manager;
    private Data data;
    public RemoveLower(CollectionManager manager, Data data) {
        this.manager=manager;
        this.data = data;
    }

    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress, Route route, String login) throws InterruptedException, SQLException {
     Runnable delete = () -> {
        if (manager.col.size() != 0) {
            if (manager.col.removeIf(dis -> dis.getDistance() < ((route).getDistance()))) {
                try {
                    data.deleteById((route).getId(),login);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                poolSend.submit(new Sender(datagramSocket,inetSocketAddress, "Нема меньших"));
            }
        }
        poolSend.submit(new Sender(datagramSocket,inetSocketAddress,"ЕлеМенты успешно удалены"));
    };
     FTP.execute(delete);
    }
}