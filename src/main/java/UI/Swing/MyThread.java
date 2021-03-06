package UI.Swing;

import communication.UDPMessageReceiverService;
import communication.UDPMessageSenderService;
import structure.Conversation;
import structure.Database.LocalDatabase;
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
                if(!u.getIdentifiant().equals(mainWindow.getMainUtilisateur().getIdentifiant())) {
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
                udpMessageReceiverService.listenOnPort(port, message -> {
                    if(message.equals(""))
                        mainWindow.updateUtilisateursConnectes();
                    else{
                        Utilisateur destinataire = LocalDatabase.getUtilisateur(message.split(" ")[0]);
                        mainWindow.updateActiveConversation(new Conversation(mainWindow.getMainUtilisateur(), destinataire));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
