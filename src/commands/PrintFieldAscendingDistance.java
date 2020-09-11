package commands;


import Foundation.Route;
import proga.CollectionManager;
import proga.Sender;


import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrintFieldAscendingDistance extends AbstractCommand {
    private CollectionManager manager;
    public PrintFieldAscendingDistance(CollectionManager manager) {
        this.manager=manager;

    }

    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , SocketAddress inetSocketAddress) throws NumberFormatException, InterruptedException {
    Runnable printfield = () -> {
        if (manager.col.size() != 0) {
            Stream<Route> stream = manager.col.stream();
            poolSend.submit(new Sender(datagramSocket,inetSocketAddress,stream.filter(col -> col.getDistance() != null).sorted((p1, p2) -> (int) (p1.getDistance() - p2.getDistance()))
                    .map(col -> "Distance" + " - " + col.getDistance()).collect(Collectors.joining("\n"))));
        }
        poolSend.submit(new Sender(datagramSocket,inetSocketAddress,"Коллекция пуста"));
    };
    FTP.execute(printfield);
    }
}