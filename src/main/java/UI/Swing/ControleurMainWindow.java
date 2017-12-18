package UI.Swing;

import communication.UDPMessageSenderService;
import structure.*;

import java.util.ArrayList;

public class ControleurMainWindow {

    private final MainWindow mainWindow;

    public ControleurMainWindow(MainWindow mainWindow){ this.mainWindow = mainWindow; }

    public void onUtilisateurClicked(Utilisateur utilisateur, Utilisateur clickedUser){
       Conversation conversation = new Conversation(utilisateur,clickedUser);
       mainWindow.updateActiveConversation(conversation);
    }

    public void onSendMessageButtonClicked(Conversation conversation, Quadruplet messageData) {
        if(!messageData.getMessage().equals("")){
            BaseDeDonnees.sauvegarderMessage(conversation,messageData);
            conversation.ajouterMessage(messageData);
            mainWindow.updateActiveConversation(conversation);
            UDPMessageSenderService udpMessageSenderService = new UDPMessageSenderService();
            try{
                udpMessageSenderService.sendMessageOn(conversation.getReceiver().getIPAddress(),conversation.getReceiver().getPort(),
                        conversation.getSender().getIdentifiant()+" "+messageData.getMessage());
            }
            catch (Exception e){}
        }
    }

    public void onSendMessageSalonButtonClicked(Salon salon, Quadruplet messageData) {
        if(!messageData.getMessage().equals("")){
            BaseDeDonnees.sauvegarderMessageSalon(salon, messageData);
            salon.ajouterMessage(messageData);
            mainWindow.updateActiveSalon(salon);
            UDPMessageSenderService udpMessageSenderService = new UDPMessageSenderService();
            try{
                udpMessageSenderService.sendMessageOn(salon.getIpAdresse(),salon.getPort(),
                        messageData.getSender()+" "+messageData.getMessage());
            }
            catch (Exception e){}
        }
    }

    public void onSalonClicked(String nom) {
        Salon salon = new Salon(nom,"239.0.0.1");
        mainWindow.updateActiveSalon(salon);
    }

    public void onChangePseudonymeButtonClicked(Utilisateur user,String pseudonyme){
        BaseDeDonnees.changerPseudonyme(user,pseudonyme);
        UDPMessageSenderService udpMessageSenderService = new UDPMessageSenderService();
        mainWindow.updatePseudonyme(pseudonyme);
        ArrayList<Utilisateur> liste = mainWindow.getListeConnecte();
        try {
            for(Utilisateur u :liste){
                if(!u.getPseudonyme().equals(mainWindow.getMainUtilisateur().getPseudonyme())) {
                    udpMessageSenderService.sendMessageOn(u.getIPAddress(), u.getPort(),"");
                    System.out.println("message envoyé a "+u.getPseudonyme());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
