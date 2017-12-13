package communication;


import UI.Terminal.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Terminal terminal = new Terminal();
        ReceiveUI receiveUI = new ReceiveUI(terminal, new MessageReceiverServiceFactory());
        SendUI sendUI = new SendUI(new MessageSenderServiceFactory(), terminal);
        StartingUI startingUI = new StartingUI(terminal, new ProtocolChooser(terminal), receiveUI, sendUI);
        startingUI.askForAction();
    }
}
