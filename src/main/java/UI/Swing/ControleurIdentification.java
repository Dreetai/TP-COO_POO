package UI.Swing;

import structure.BaseDeDonnees;
import structure.LoginUtilisateur;
import structure.Utilisateur;

import javax.swing.*;

public class ControleurIdentification {

    private final StartWindow startWindow;


    public ControleurIdentification(StartWindow startWindow) {
        this.startWindow = startWindow;
    }

    public void onConnectButtonClicked(String identifiant, String password){
        if(!identifiant.equals("") && !password.equals("")){
            if(BaseDeDonnees.testerSiLoginOK(identifiant,password)){
                Utilisateur user = new Utilisateur(identifiant,BaseDeDonnees.getPseudonyme(identifiant));
                BaseDeDonnees.addUtilisateurLocal(user);
                new MainWindow(user);
                this.startWindow.dispose();
            }else{
                JOptionPane.showMessageDialog(null,"Invalid username / password!", "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Login ou mot de passe non renseigné !", "Error",JOptionPane.ERROR_MESSAGE);
        }

    }

    public void onSignUpButtonClicked(String identifiant, String password){
        if(!identifiant.equals("") && !password.equals("")){
            if(BaseDeDonnees.testerSiAddUserOK(identifiant)){
                BaseDeDonnees.addLoginUtilisateur(identifiant,password,identifiant);
                Utilisateur user = new Utilisateur(identifiant);
                BaseDeDonnees.addUtilisateurLocal(user);
                new MainWindow(user);
                this.startWindow.dispose();
            }else{
                JOptionPane.showMessageDialog(null,"Username taken!", "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Login ou mot de passe non renseigné !", "Error",JOptionPane.ERROR_MESSAGE);
        }

    }
}
