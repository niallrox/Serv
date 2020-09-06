package commands;

import proga.CollectionManager;
import proga.ServerSender;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.Date;
import java.util.concurrent.ExecutorService;

public class Info extends AbstractCommand {
    private Date initDate = new Date();
    private CollectionManager manager;
    private String answer;
    private ExecutorService poolSend;

    public Info(CollectionManager manager) {
        this.manager = manager;
    }

    /**
     * Метод показывает информацию о коллекции
     *
     * @return
     */
    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress) throws InterruptedException {
        Runnable info = () -> {
            answer = "Тип коллекции - PriorityQueue\n" +
                    "Дата инициализации " + initDate + "\n" +
                    "Размер коллекции " + manager.col.size();
            poolSend.submit(new ServerSender(datagramSocket, inetSocketAddress, answer));
        };
        FTP.execute(info);
    }
}
