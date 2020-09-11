package commands;


import Foundation.Route;
import proga.Data;
import proga.CollectionManager;
import proga.Sender;


import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class Update extends AbstractCommand {
    private CollectionManager manager;
    private Data data;

    public Update(CollectionManager manager, Data data) {
        this.manager = manager;
        this.data = data;
    }


    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetAddress inetSocketAddress, String str, Route route, String login) throws NumberFormatException, InterruptedException {
        Runnable update = () -> {
            try {
                if (!(route == null)) {
                    if (!(manager.col.size() == 0)) {
                        long id = Long.parseLong(str);
                        data.update(id, login);
                        if (manager.col.removeIf(col -> col.getId() == id && col.getLogin().equals(login))) {
                            route.setId(id);
                            route.setLogin(login);
                            manager.col.add(route);
                            poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Элемент обновлен"));
                        } else {
                            poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Элемента с таким id нет или пользователь не имеет доступа к этому элементу"));
                        }
                    } else {
                        poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Коллекция пуста"));
                    }
                } else {
                    poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Ошибка при добавлении элемента. Поля указаны не верно"));
                }
            } catch (SQLException e) {
                poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Ошибка при работе с БД (вероятно что-то с БД)"));
            } catch (NullPointerException e) {
                poolSend.submit(new Sender(datagramSocket , inetSocketAddress, "Данные в скрипте введены не верно"));
            }
        };
        FTP.execute(update);
    }
}