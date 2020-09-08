package proga;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Sender implements Runnable {
    private DatagramSocket datagramSocket;
    private InetSocketAddress inetAddress;
    private String answer;

    public Sender(DatagramSocket datagramSocket, InetSocketAddress inetSocketAddress, String answer) {
        this.datagramSocket =datagramSocket;
        this.inetAddress=inetSocketAddress;
        this.answer = answer;
    }


    public void run() {
        try {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(answer);
                objectOutputStream.flush();
                byte[] sendbuf = byteArrayOutputStream.toByteArray();
                DatagramPacket datagramPacket = new DatagramPacket(sendbuf, sendbuf.length, inetAddress);
                datagramSocket.send(datagramPacket);
            }
        } catch (IOException e) {
//aasasasasa
        }
    }
}