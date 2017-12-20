package UI.Swing;

import communication.UDPMessageSenderService;
import structure.BaseDeDonnees;
import structure.Utilisateur;

import java.awt.event.*;
import java.util.ArrayList;

public class ControleurWindow extends WindowAdapter {

    private MainWindow mainWindow;

    public ControleurWindow (MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    public void windowClosing(WindowEvent e) {
        BaseDeDonnees.deleteUtilisateurLocal(this.mainWindow.getMainUtilisateur());
        UDPMessageSenderService udpMessageSenderService = new UDPMessageSenderService();
        ArrayList<Utilisateur> liste = mainWindow.getListeConnecte();
        try {
            for(Utilisateur u :liste){
                if(!u.getIdentifiant().equals(mainWindow.getMainUtilisateur().getIdentifiant())) {
                    udpMessageSenderService.sendMessageOn(u.getIPAddress(), u.getPort(), "");
                    System.out.println("message fin envoyé a " + u.getPseudonyme());
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        System.exit(0);
    }
}
