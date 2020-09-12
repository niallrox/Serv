package proga;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class Sender implements Runnable {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private String answer;
    private SocketAddress s;

    public Sender(DatagramSocket datagramSocket, SocketAddress inetSocketAddress, String answer) {
        this.datagramSocket = datagramSocket;
        this.s = inetSocketAddress;
        this.answer = answer;
    }


    public void run() {
        try {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(answer);
                objectOutputStream.flush();
                byte[] sendbuf = byteArrayOutputStream.toByteArray();
                DatagramPacket datagramPacket = new DatagramPacket(sendbuf, sendbuf.length, s);
                datagramSocket.send(datagramPacket);
            }
        } catch (IOException e) {
        }
    }

}
// java18 -jar Client.jar
// java18 -jar Serv.jar database.properties