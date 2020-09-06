package proga;

import Foundation.*;
import commands.*;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class CollectionManager {
    public Map<String, AbstractCommand> commandMap;
    public Collection<Route> col = new CopyOnWriteArrayList(new LinkedList<>());



    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Запуск сервера");
        System.out.println("hi,type port");
        ServerConnection serverConnection = new ServerConnection();
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    System.out.println("Отключение сервера");
                }
            });
            serverConnection.connection(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Вы не ввели имя файла");
        } catch (NoSuchElementException e) {
            //Для ctrl+D
        }
    }


    public void loadCommands(CollectionManager manager, Data data) {
        commandMap = new HashMap<>();
        commandMap.put("clear", new Clear(manager, data));
        commandMap.put("show", new Show(manager));
        commandMap.put("info", new Info(manager));
        commandMap.put("help", new Help());
        commandMap.put("remove_head", new RemoveHead(manager, data));
        commandMap.put("print_field_ascending_distance distance", new PrintFieldAscendingDistance(manager));
        commandMap.put("min_by_distance", new MinByDistance(manager));
        commandMap.put("remove_by_id", new RemoveId(manager, data));
        commandMap.put("add", new Add(manager, data));
        commandMap.put("remove_lower", new RemoveLower(manager, data));
        commandMap.put("max_by_from", new MaxByFrom(manager));
        commandMap.put("add_if_max", new AddIfMax(manager, data));
        commandMap.put("update", new Update(manager, data));
    }


    public void loadToCol(String file, Data data) throws ClassNotFoundException {
        try {
            col = data.loadFromSQL(file);
        } catch (SQLException e) {
            System.out.println("Сервер не подключился к БД");
        } catch (IOException e) {
            System.out.println("Сервер не подключился к БД");
        } catch (NullPointerException e) {
        }
    }
}