package commands;

import proga.CollectionManager;
import proga.Sender;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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


    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , SocketAddress inetSocketAddress) throws InterruptedException {
        Runnable info = () -> {
            answer = "Тип коллекции - LinkedList\n" +
                    "Дата инициализации " + initDate + "\n" +
                    "Размер коллекции " + manager.col.size();
            poolSend.submit(new Sender(datagramSocket, inetSocketAddress, answer));
        };
        FTP.execute(info);
    }
}
