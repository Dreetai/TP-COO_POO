package main.structure;

import main.communication.UDPMessageReceiverService;
import main.communication.UDPMessageSenderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conversation {

    private List<Quadruplet> historique;
    private Utilisateur sender;
    private Utilisateur receiver;

    public Conversation(Utilisateur utilisateur1,Utilisateur utilisateur2){
        this.historique = new ArrayList<Quadruplet>();
        this.sender = utilisateur1;
        this.receiver = utilisateur2;
        BaseDeDonnees.recupererHistorique(this);
    }

    public void finirConversation(){

    }

    public void envoyerMessage(String message) throws Exception{

        Quadruplet data = new Quadruplet(message,sender.getIdentifiant(),receiver.getIdentifiant(),new Date().toString());
        BaseDeDonnees.sauvegarderMessage(this,data);
        UDPMessageSenderService udpMessageSenderService = new UDPMessageSenderService();
        udpMessageSenderService.sendMessageOn(receiver.getIPAddress(),2000,message);
    }
    public void recevoirMessage(String message) throws Exception{
        UDPMessageReceiverService udpMessageReceiverService = new UDPMessageReceiverService();
        //udpMessageReceiverService.listenOnPort(2000);

    }

    public void ajouterMessage(Quadruplet data){
        this.historique.add(data);
    }

    public Utilisateur getSender() {
        return sender;
    }

    public Utilisateur getReceiver() {
        return receiver;
    }

    public List<Quadruplet> getHistorique() {
        return historique;
    }
}
