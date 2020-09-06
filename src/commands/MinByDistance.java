package commands;


import Foundation.Route;
import proga.CollectionManager;
import proga.ServerSender;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

public class MinByDistance extends AbstractCommand {
    private CollectionManager manager;
    public MinByDistance(CollectionManager manager) {
        this.manager=manager;
    }

    @Override
    public void executeCommand(ExecutorService FTP ,ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress) throws NumberFormatException, InterruptedException {
    Runnable minbydistance = () -> {
        if (manager.col.size() != 0) {
            Route min = manager.col.stream().min(Route::compareTo).get();
            poolSend.submit(new ServerSender(datagramSocket,inetSocketAddress,min.toString()));
        } else {
            poolSend.submit(new ServerSender(datagramSocket,inetSocketAddress,"Коллекция пуста"));
        }
    };
    FTP.execute(minbydistance);
    }
}
