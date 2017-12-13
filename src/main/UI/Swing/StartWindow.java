package main.UI.Swing;


import main.structure.Utilisateur;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class StartWindow extends JFrame {

    private ControleurIdentification controleurIdentification;

    public StartWindow(){

        this.controleurIdentification = new ControleurIdentification(this);
        this.initialiserJFrame();
        this.setTitle("Authentification");
        this.setSize(250, 150);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void initialiserJFrame(){
        JPanel mainPanel = new JPanel(new FlowLayout());

        JTextField identifiantJTextField = new JTextField(15);
        JLabel identifiantJLabel = new JLabel("Identifiant:");
        identifiantJLabel.setLabelFor(identifiantJTextField);
        ((AbstractDocument) identifiantJTextField.getDocument()).setDocumentFilter(new CharDocumentFilter());

        JPasswordField passwordField = new JPasswordField(15);
        JLabel passwordJLabel = new JLabel("Password:");
        passwordJLabel.setLabelFor(passwordField);
        ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new CharDocumentFilter());

        JButton signUpJButton = new JButton("Sign up");
        signUpJButton.addActionListener(e -> {
            try {
                String asd = new String(passwordField.getPassword());
                Utilisateur mainUtilisateur = this.controleurIdentification.onSignUpButtonClicked(identifiantJTextField.getText(),
                        new String(passwordField.getPassword()));
                if (mainUtilisateur!=null){
                    new MainWindow(mainUtilisateur);
                    this.dispose();
                }
            } catch (Exception exce) {
                exce.printStackTrace();
            }
        });

        JButton identifiantJButton = new JButton("Connect");
        identifiantJButton.addActionListener(e -> {
                try {
                    Utilisateur mainUtilisateur = this.controleurIdentification.onConnectButtonClicked(identifiantJTextField.getText(), new String(passwordField.getPassword()));
                    if (mainUtilisateur!=null){
                        new MainWindow(mainUtilisateur);
                        this.dispose();
                    }

                } catch (Exception exce) {
                    exce.printStackTrace();
                }
            });

        this.setLayout(new GridLayout(0,1));
        mainPanel.add(identifiantJLabel);
        mainPanel.add(identifiantJTextField);
        mainPanel.add(passwordJLabel);
        mainPanel.add(passwordField);
        mainPanel.add(signUpJButton);
        mainPanel.add(identifiantJButton);
        this.add(mainPanel);

    }

    public static void main (String [] args){
        new StartWindow();
    }
}