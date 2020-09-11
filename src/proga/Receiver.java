package proga;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class Receiver implements Callable<Command> {
    private CollectionManager manager;
    private Data data;
    private ExecutorService poolSend;
    private Handler serverHandler = new Handler();
    private byte[] sendbuf;
    private DatagramSocket datagramSocket;

    public Receiver(DatagramSocket datagramSocket, byte[] sendbuf, CollectionManager manager, Data data, ExecutorService poolSend) {
        this.datagramSocket = datagramSocket;
        this.sendbuf = sendbuf;
        this.manager = manager;
        this.data = data;
        this.poolSend = poolSend;
    }

    private InetAddress inetAddress;
    private int port;

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }


    @Override
    public Command call() {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(sendbuf, sendbuf.length);
            datagramSocket.receive(datagramPacket);
            inetAddress = datagramPacket.getAddress();
            port = datagramPacket.getPort();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
            ObjectInputStream fromServer = new ObjectInputStream(byteArrayInputStream);
            Command command = (Command) fromServer.readObject();
            byteArrayInputStream.close();
            fromServer.close();
            return command;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    return null;}
}
