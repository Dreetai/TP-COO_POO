package UI.Swing;



import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class StartWindow extends JFrame {

    private ControleurIdentification controleurIdentification;

    public StartWindow(){

        this.controleurIdentification = new ControleurIdentification(this);
        this.initialiserJFrame();
        this.setTitle("Authentification");
        this.setSize(275, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void initialiserJFrame(){
        JPanel mainPanel = new JPanel(new GridLayout(3,1));
        mainPanel.setBackground(Color.WHITE);

        JTextField identifiantJTextField = new JTextField(15);
        identifiantJTextField.setBorder(BorderFactory.createTitledBorder("Identifiant"));
        ((AbstractDocument) identifiantJTextField.getDocument()).setDocumentFilter(new CharDocumentFilter());

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createTitledBorder("Mot de passe"));
        passwordField.addActionListener(e ->
                this.controleurIdentification.onConnectButtonClicked(identifiantJTextField.getText(), new String(passwordField.getPassword()))
        );

        JPanel buttonJPanel = new JPanel();
        buttonJPanel.setBackground(Color.WHITE);
        JButton signUpJButton = new JButton("Inscription");
        signUpJButton.setFocusable(false);
        signUpJButton.addActionListener(e ->
                this.controleurIdentification.onSignUpButtonClicked(identifiantJTextField.getText(),new String(passwordField.getPassword()))
        );

        JButton identifiantJButton = new JButton("Connexion");
        identifiantJButton.setFocusable(false);
        identifiantJButton.addActionListener(e ->
                this.controleurIdentification.onConnectButtonClicked(identifiantJTextField.getText(), new String(passwordField.getPassword()))
        );
        buttonJPanel.add(signUpJButton);
        buttonJPanel.add(identifiantJButton);

        mainPanel.add(identifiantJTextField);
        mainPanel.add(passwordField);
        mainPanel.add(buttonJPanel);
        this.add(mainPanel);

    }

    public static void main (String [] args){
        new StartWindow();
    }
}