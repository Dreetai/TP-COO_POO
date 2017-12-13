package main.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPMessageReceiverService implements MessageReceiverService {
    

    private ServerSocket serverSocket;
    private Socket chatSocket;

    public TCPMessageReceiverService(){

    }

	@Override
    public void listenOnPort(int port, IncomingMessageListener incomingMessageListener) throws Exception {

        this.serverSocket = new ServerSocket(port);
        this.chatSocket = serverSocket.accept();
		InputStreamReader stream = new InputStreamReader(chatSocket.getInputStream());
        BufferedReader reader = new BufferedReader(stream);
        String message = reader.readLine();

        incomingMessageListener.onNewIncomingMessage(message);

        serverSocket.close();
        chatSocket.close();
    }
}
