package communication;

import java.io.PrintWriter;
import java.net.*;

public class TCPMessageSenderService implements MessageSenderService{

    @Override
   	public void sendMessageOn(String ipAddress, int port, String message) throws Exception {
        Socket senderSocket = new Socket(ipAddress, port);
        PrintWriter writer = new PrintWriter(senderSocket.getOutputStream());
        writer.println(message);
        writer.close();
        senderSocket.close();
    }
}
