package main.UI.Swing;

import main.structure.BaseDeDonnees;
import main.structure.InvalidCSVFileException;
import main.structure.LoginUtilisateur;
import main.structure.Utilisateur;

import javax.rmi.CORBA.Util;
import javax.swing.*;
import java.io.IOException;

public class ControleurIdentification {

    private final StartWindow startWindow;


    public ControleurIdentification(StartWindow startWindow) {
        this.startWindow = startWindow;
    }

    public Utilisateur onConnectButtonClicked(String identifiant, String password) throws IOException, InvalidCSVFileException {
        LoginUtilisateur loginUser = BaseDeDonnees.testerSiLoginOK(identifiant,password);
        if(loginUser != null){
            Utilisateur user = new Utilisateur(identifiant,loginUser.getPseudonyme());
            BaseDeDonnees.addPseudonyme(user);
            return user;

        }else{
            JOptionPane.showMessageDialog(null,"Invalid username / password!", "Error",JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public Utilisateur onSignUpButtonClicked(String identifiant, String password) throws IOException, InvalidCSVFileException {
        if(BaseDeDonnees.testerSiAddUserOK(identifiant)){
            LoginUtilisateur loginUser = new LoginUtilisateur(identifiant,password,identifiant);
            BaseDeDonnees.addUtilisateur(loginUser);
            Utilisateur user = new Utilisateur(identifiant);
            BaseDeDonnees.addPseudonyme(user);
            return user;
        }else{
            JOptionPane.showMessageDialog(null,"Username taken!", "Error",JOptionPane.ERROR_MESSAGE);
            return null;
        }
        }
}
