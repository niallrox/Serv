package proga;


import Foundation.*;


import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Properties;

public class Data {
    private ResultSet res;
    private java.sql.Connection connect;
    private PreparedStatement ps;
    private Statement statement;
    private MessageDigest hash;


    public LinkedList<Route> loadFromSQL(String file) throws ClassNotFoundException, IOException, SQLException, NullPointerException {
        LinkedList<Route> col = new LinkedList<>();
        CollectionManager collectionManager = new CollectionManager();
        FileInputStream bd = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(bd);
        String url = properties.getProperty("location");
        String login = properties.getProperty("username");
        String password = properties.getProperty("password");
        Class.forName("org.postgresql.Driver");
        connect = DriverManager.getConnection(url, login, password);
        statement = connect.createStatement();
        res = statement.executeQuery("SELECT * FROM route;");
        while (res.next()) {
            int id = res.getInt("id");
            String name = res.getString("name");
            Integer x = res.getInt("coordX");
            Integer y = res.getInt("coordY");
            Float locFromX = res.getFloat("locFromX");
            Double locFromY = res.getDouble("locFromY");
            Integer locFromZ = res.getInt("locFromZ");
            String locFromName = res.getString("locFromName");
            Float locToX = res.getFloat("locToX");
            Double locToY = res.getDouble("locToY");
            Integer locToZ = res.getInt("locToZ");
            String locToName = res.getString("locToName");
            Long distance = res.getLong("distance");
            String loginSG = res.getString("login");
            Route route = new Route(id, name, new Coordinates(x, y), new Location(locFromX, locFromY, locFromZ,locFromName), new Location(locToX,locToY,locToZ,locToName),distance,loginSG);
            col.add(route);
        }
        System.out.println("Сервер подключился к БД");
        return col;
    }


    public String registration(Command command) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            hash = MessageDigest.getInstance("MD5");
            PreparedStatement ps = connect.prepareStatement("INSERT INTO login_password (login, password) VALUES (?, ?);");
            ps.setString(1, command.getLogin());
            ps.setString(2, Base64.getEncoder().encodeToString(hash.digest(command.getPassword().getBytes("UTF-8"))));
            ps.execute();
            System.out.println("Пользователь с логином " + command.getLogin() + " успешно зарегестрирован");
            return "Регистрация прошла успешно";
        } catch (SQLException e) {
            System.out.println("Ошибка при работе с БД (вероятно что-то с БД)");
            return "Ошибка с бд регистрация не удалась (скорее всего такой пользователь уже существует)";
        }
    }


    public boolean authorization(Command command) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            hash = MessageDigest.getInstance("MD5");
            ps = connect.prepareStatement("SELECT * FROM login_password WHERE (login = ?);");
            ps.setString(1, command.getLogin());
            res = ps.executeQuery();
            res.next();
            return Base64.getEncoder().encodeToString(hash.digest(command.getPassword().getBytes("UTF-8")))
                    .equals(res.getString("password"));
        } catch (SQLException e) {
            System.out.println("Ошибка при работе с БД (вероятно что-то с БД)");
            return false;
        }
    }


    public void addToSQL(Route route, String login, int id) throws SQLException, NullPointerException {
        ps = connect.prepareStatement("INSERT INTO route (id, name, coordX, coordY, " +
                "creationDate, locFromX, locFromY, locFromZ, locFromName, locToX, locToY,locToZ,locToName,distance,login) " +
                "VALUES (currval('idSGsequence'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, route.getName());
        ps.setInt(2, route.getCoordinates().getX());
        ps.setInt(3, route.getCoordinates().getY());
        ps.setObject(4, route.getCreationDate());
        ps.setFloat(5, route.getFrom().getX());
        ps.setDouble(6, route.getFrom().getY());
        ps.setInt(7, route.getFrom().getZ());
        ps.setString(8, route.getFrom().getName());
        ps.setFloat(9, route.getTo().getX());
        ps.setDouble(10, route.getTo().getY());
        ps.setInt(11, route.getTo().getZ());
        ps.setString(12, route.getFrom().getName());
        ps.setLong(13, route.getDistance());
        ps.setString(14, login);
        ps.execute();
    }


    public int getSQLId() throws SQLException {
        ResultSet res = statement.executeQuery("SELECT nextval('idSGsequence');");
        res.next();
        return res.getInt(1);
    }
    public long getMAXId() throws SQLException {
        res = statement.executeQuery("SELECT MAX(id) FROM route");
        res.next();
        int id = res.getInt(1);
        return id;
    }


    public void clearSQL(String login) throws SQLException {
        ps = connect.prepareStatement("DELETE FROM route WHERE login = ?;");
        ps.setString(1, login);
        ps.execute();
    }


    public void deleteById(Integer id, String login) throws SQLException {
        ps = connect.prepareStatement("DELETE FROM route WHERE(id = ?) AND (login = ?)");
        ps.setInt(1, id);
        ps.setString(2, login);
        ps.execute();
    }



    public void update(Route route, int id,String login) throws SQLException {
        ps = connect.prepareStatement("UPDATE route SET name = ? , coordX = ? , coordY = ?" +
                ", creationDate = ?, locFromX = ? , locFromY = ?, locFromZ = ?, locFromName = ?, locToX = ?, locToY = ?, locToZ = ?, locToName = ?, distance = ?" +
                "WHERE id = ? AND login = ?;");
        ps.setString(1, route.getName());
        ps.setInt(2, route.getCoordinates().getX());
        ps.setInt(3, route.getCoordinates().getY());
        ps.setObject(4, route.getCreationDate());
        ps.setFloat(5, route.getFrom().getX());
        ps.setDouble(6,route.getFrom().getY());
        ps.setInt(7, route.getFrom().getZ());
        ps.setString(8, route.getFrom().getName());
        ps.setFloat(9, route.getTo().getX());
        ps.setDouble(10,route.getTo().getY());
        ps.setInt(11, route.getTo().getZ());
        ps.setString(12, route.getTo().getName());
        ps.setLong(13, route.getDistance());
        ps.setInt(14, id);
        ps.setString(15,login);
        ps.execute();
    }
}