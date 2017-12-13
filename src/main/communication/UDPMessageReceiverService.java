package main.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class UDPMessageReceiverService implements MessageReceiverService {

	public static final int BUFFER_SIZE = 1024;

    @Override
    public void listenOnPort(int port, IncomingMessageListener incomingMessageListener) throws Exception {
        DatagramSocket receiverSocket = new DatagramSocket(port);
        DatagramPacket receivedPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        receiverSocket.receive(receivedPacket);
        byte[] data = new byte[receivedPacket.getLength()];
        System.arraycopy(receivedPacket.getData(), receivedPacket.getOffset(), data, 0, receivedPacket.getLength());

        incomingMessageListener.onNewIncomingMessage(new String(data, "UTF-8"));

        receiverSocket.close();
    }
}
