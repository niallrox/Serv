package commands;

import Foundation.Route;


import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AbstractCommand {

    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress) throws InterruptedException {
    }

    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress, String login) throws InterruptedException {
    }

    public void executeCommand(ExecutorService FTP,ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress, String str, String login) throws InterruptedException {
    }

    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress, Route route, String login) throws InterruptedException, ExecutionException, SQLException {
    }

    public void executeCommand(ExecutorService FTP,ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress, String str, Route route, String login) throws InterruptedException, SQLException {
    }
}