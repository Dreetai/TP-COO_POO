package UI.Swing;

import communication.IncomingMessageListener;
import communication.UDPMessageReceiverService;
import communication.UDPMessageSenderService;
import structure.BaseDeDonnees;
import structure.Conversation;
import structure.Utilisateur;

import java.util.ArrayList;

public class MyThread extends Thread{

    private MainWindow mainWindow;
    private int port;
    private UDPMessageReceiverService udpMessageReceiverService;
    private UDPMessageSenderService udpMessageSenderService;

    public MyThread(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        this.port = mainWindow.getMainUtilisateur().getPort();
        this.udpMessageReceiverService = new UDPMessageReceiverService();
        this.udpMessageSenderService = new UDPMessageSenderService();
        ArrayList <Utilisateur> liste = mainWindow.getListeConnecte();
        try {
            for(Utilisateur u :liste){
                if(!u.getPseudonyme().equals(mainWindow.getMainUtilisateur().getPseudonyme())) {
                    udpMessageSenderService.sendMessageOn(u.getIPAddress(), u.getPort(), "");
                    System.out.println("message envoyé a " + u.getPseudonyme());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                udpMessageReceiverService.listenOnPort(port, new IncomingMessageListener() {
                    @Override
                    public void onNewIncomingMessage(String message) {
                        if(message.equals(""))
                            mainWindow.updateUtilisateursConnectes();
                        else{
                            Utilisateur destinataire = BaseDeDonnees.getUtilisateur(message.split(" ")[0]);
                            mainWindow.initialiserConversation(new Conversation(mainWindow.getMainUtilisateur(), destinataire));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
