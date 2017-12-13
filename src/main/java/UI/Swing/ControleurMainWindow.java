package UI.Swing;

import communication.UDPMessageSenderService;
import structure.BaseDeDonnees;
import structure.Conversation;
import structure.Quadruplet;
import structure.Utilisateur;

import java.util.ArrayList;

public class ControleurMainWindow {

    private final MainWindow mainWindow;

    public ControleurMainWindow(MainWindow mainWindow){ this.mainWindow = mainWindow; }

    public void onUtilisateurClicked(Utilisateur utilisateur, Utilisateur clickedUser){
       Conversation conversation = new Conversation(utilisateur,clickedUser);
       mainWindow.initialiserConversation(conversation);
    }

    public void onSendMessageButtonClicked(Conversation conversation, Quadruplet messageData) {
        if(!messageData.getMessage().equals("")){
            BaseDeDonnees.sauvegarderMessage(conversation,messageData);
            conversation.ajouterMessage(messageData);
            mainWindow.updateActiveConversation(messageData);
            UDPMessageSenderService udpMessageSenderService = new UDPMessageSenderService();
            try{
                udpMessageSenderService.sendMessageOn(conversation.getReceiver().getIPAddress(),conversation.getReceiver().getPort(),
                        conversation.getSender().getIdentifiant()+" "+messageData.getMessage());
            }
            catch (Exception e){

            }
        }
    }

    public void onChangePseudonymeButtonClicked(Utilisateur user,String pseudonyme){
        BaseDeDonnees.changerPseudonyme(user,pseudonyme);
        UDPMessageSenderService udpMessageSenderService = new UDPMessageSenderService();
        mainWindow.updatePseudonymeLabel(pseudonyme);
        ArrayList<Utilisateur> liste = mainWindow.getListeConnecte();
        try {
            for(Utilisateur u :liste){
                udpMessageSenderService.sendMessageOn(u.getIPAddress(), u.getPort(),"");
                System.out.println("message envoyé a "+u.getPseudonyme());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
