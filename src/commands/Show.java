package commands;

import Foundation.Route;
import proga.CollectionManager;
import proga.ServerSender;


import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Show extends AbstractCommand {
    private CollectionManager manager;
    private String answer;

    public Show(CollectionManager manager) {
        this.manager = manager;
    }

    /**
     * Метод выводит элементы коллекции
     *
     * @return
     */
    @Override
    public void executeCommand(ExecutorService FTP ,ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress) throws InterruptedException {
        Runnable show = () -> {
            if (manager.col.size() != 0) {
                Stream<Route> stream = manager.col.stream();
                poolSend.submit(new ServerSender(datagramSocket , inetSocketAddress, stream.map(Route::toString).collect(Collectors.joining("\n"))));
            } else {
                poolSend.submit(new ServerSender(datagramSocket , inetSocketAddress, "Коллекция пуста."));
            }
        };
        FTP.execute(show);
    }
}