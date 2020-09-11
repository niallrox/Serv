package commands;

import Foundation.Route;
import proga.CollectionManager;
import proga.Sender;


import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class MaxByFrom extends AbstractCommand {
    private CollectionManager manager;
    public MaxByFrom(CollectionManager manager) {
        this.manager=manager;
    }

    @Override
    public void executeCommand(ExecutorService FTP , ExecutorService poolSend, DatagramSocket datagramSocket , SocketAddress inetSocketAddress) throws InterruptedException {
       Runnable maxbyfrom = () -> {  if (!(manager.col.size() == 0)) {
            Stream<Route> stream = manager.col.stream();
            Route max = stream
                    .max(Comparator.comparingInt(p -> p.getFromSum())).get();
            poolSend.submit(new Sender( datagramSocket,inetSocketAddress,  "Элемент с максимальным значением From ето " + max));
        } else {
            poolSend.submit(new Sender(datagramSocket,inetSocketAddress, "Коллекция пуста"));
        }
    };
      FTP.execute(maxbyfrom);
    }
}