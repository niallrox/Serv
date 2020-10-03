package commands;


import Foundation.Route;
import proga.Data;
import proga.CollectionManager;
import proga.Sender;


import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class AddIfMax extends AbstractCommand {
    private CollectionManager manager;
    private Data data;
    private String answer;

    public AddIfMax(CollectionManager manager, Data data) {
        this.manager = manager;
        this.data = data;
    }


    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , SocketAddress inetSocketAddress, Route route, String login) throws InterruptedException {
        Runnable addElement = () -> {
            if (manager.col.size() != 0) {
                Stream<Route> stream = manager.col.stream();
                Long maxDistance = stream.filter(col -> col.getDistance() != null)
                        .max(Comparator.comparingLong(p -> p.getDistance())).get().getDistance();
                if (route.getDistance() != null && route.getDistance() > maxDistance) {
                    try {
                        int id = data.getSQLId();
                        data.addToSQL(route, login, id);
                        route.setId(id);
                        route.setLogin(login);
                        manager.col.add(route);
                        poolSend.submit(new Sender(datagramSocket, inetSocketAddress , "Элемент коллекции добавлен"));
                    } catch (SQLException e) {
                        poolSend.submit(new Sender(datagramSocket, inetSocketAddress , "Ошибка при работе с БД (вероятно что-то с БД)"));
                    } catch (NullPointerException e) {
                        poolSend.submit(new Sender(datagramSocket, inetSocketAddress , "Данные в скрипте введены не верно"));
                    }
                } else {
                    poolSend.submit(new Sender(datagramSocket, inetSocketAddress , "Элемент коллекции не сохранен, так как его distance меньше " +
                            "distance других элементов коллекции больше или равен null"));
                }
            } else {
                poolSend.submit(new Sender(datagramSocket, inetSocketAddress , "Коллекция пуста"));
            }
        };
        FTP.execute(addElement);
    }
}