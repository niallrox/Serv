package proga;



import Foundation.*;

import java.io.Serializable;

/**
 * Класс для передачи команд в виде объекта
 */
public class Command implements Serializable,Runnable {
    private static final long serialVersionUID = 17L;
    private String name;
    private String args;
    private Route route;
    private String login;
    private String password;

    public Command(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Command(String name, Route route, String login, String password) {
        this.name = name;
        this.route = route;
        this.login = login;
        this.password = password;
    }

    public Command(String name, String args, Route route, String login, String password) {
        this.name = name;
        this.args = args;
        this.route = route;
        this.login = login;
        this.password = password;
    }

    public Command(String name, String args, String login, String password) {
        this.name = name;
        this.args = args;
        this.login = login;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getArgs() {
        return args;
    }

    public Route getRoute() {return route;}

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void run() {

    }
}