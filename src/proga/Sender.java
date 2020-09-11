package proga;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.Callable;

public class Sender implements Callable<DatagramPacket> {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private String answer;
    private SocketAddress s;

    public Sender(DatagramSocket datagramSocket, InetAddress inetSocketAddress, String answer) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetSocketAddress;
        this.answer = answer;
    }


    public DatagramPacket call() {
        try {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(answer);
                objectOutputStream.flush();
                byte[] sendbuf = byteArrayOutputStream.toByteArray();
                s = new InetSocketAddress(inetAddress, CollectionManager.port);
                DatagramPacket datagramPacket = new DatagramPacket(sendbuf, sendbuf.length, s);
                datagramSocket.send(datagramPacket);
                System.out.println("ssss213124");
                return datagramPacket;
            }
        } catch (IOException e) {
        }
    return null;}
}