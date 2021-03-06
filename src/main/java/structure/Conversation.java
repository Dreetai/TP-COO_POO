package structure;

import communication.UDPMessageReceiverService;
import communication.UDPMessageSenderService;
import structure.Database.MessageDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conversation {

    private List<Quadruplet> historique;
    private Utilisateur sender;
    private Utilisateur receiver;

    public Conversation(Utilisateur utilisateur1,Utilisateur utilisateur2) {
        this.historique = new ArrayList<>();
        this.sender = utilisateur1;
        this.receiver = utilisateur2;
        MessageDatabase.recupererHistorique(this);
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
