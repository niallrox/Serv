package proga;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Connection {
    private CollectionManager manager = new CollectionManager();
    private Data data = new Data();
    private ExecutorService poolSend = Executors.newCachedThreadPool();
    private ExecutorService poolReceiver = Executors.newCachedThreadPool();
    private byte[] buf = new byte[4096];
    private DatagramSocket datagramSocket;
    private Handler handler = new Handler();
    private SocketAddress socketAddress;

    public Connection(DatagramSocket datagramSocket, SocketAddress socketAddress) {
        this.datagramSocket = datagramSocket;
        this.socketAddress = socketAddress;

    }


    public void connection(String file) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        System.out.println("Сервер запущен.");
        manager.loadCommands(manager, data);
        manager.loadToCol(file, data);
        while (true) {
            Receiver r = new Receiver(datagramSocket, buf, manager, data, poolSend);
            Future<Command> f = poolReceiver.submit(r);
            Command command = f.get();
            SocketAddress socketAddress = new InetSocketAddress(r.getInetAddress(),r.getPort());
            handler.handler(command, manager, data, poolSend, datagramSocket, socketAddress);
        }
    }
}

