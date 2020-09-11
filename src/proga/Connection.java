package proga;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
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

    public Connection(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }


    public void connection(String file) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        System.out.println("Сервер запущен.");
        manager.loadCommands(manager, data);
        manager.loadToCol(file, data);
        while (true) {
            Receiver r = new Receiver(datagramSocket, buf, manager, data, poolSend);
            Future<Command> f = poolReceiver.submit(r);
            System.out.println(r.getInetAddress() +" " +datagramSocket.getInetAddress() );
            handler.handler(f.get(), manager, data, poolSend, datagramSocket, r.getInetAddress());
        }
    }
}

