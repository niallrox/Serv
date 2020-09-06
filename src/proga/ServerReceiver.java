package proga;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

public class ServerReceiver implements Runnable {
    private CollectionManager manager;
    private Data data;
    private ExecutorService poolSend;
    private ServerHandler serverHandler = new ServerHandler();
    private byte[] sendbuf;
    private DatagramSocket datagramSocket;

    public ServerReceiver(DatagramSocket datagramSocket, byte[] sendbuf, CollectionManager manager, Data data, ExecutorService poolSend) {
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
    public void run() {
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
            serverHandler.handler(command, manager, data, poolSend, datagramSocket, new InetSocketAddress(inetAddress, port));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
