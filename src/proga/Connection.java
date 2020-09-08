package proga;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {
    private CollectionManager manager = new CollectionManager();
    private Data data = new Data();
    private Scanner scanner = new Scanner(System.in);
    private ExecutorService poolSend = Executors.newCachedThreadPool();
    private ExecutorService poolReceiver = Executors.newCachedThreadPool();
    private int port = Integer.parseInt(scanner.nextLine());
    private byte[] buf = new byte[4096];


    public void connection(String file) throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Сервер запущен.");
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            while (true) {
                manager.loadCommands(manager, data);
                manager.loadToCol(file, data);
                poolReceiver.submit(new Receiver(datagramSocket, buf, manager, data, poolSend));
            }
        }
    }
}
