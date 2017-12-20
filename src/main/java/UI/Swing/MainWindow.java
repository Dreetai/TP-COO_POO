package UI.Swing;


import com.sun.org.apache.xpath.internal.SourceTree;
import communication.UDPMessageSenderService;
import structure.*;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainWindow extends JFrame {

    private final Utilisateur mainUtilisateur;
    private ArrayList<Utilisateur> listeConnecte;
    private ControleurMainWindow controleurMainWindow;
    private ControleurWindow controleurWindow;
    private Conversation activeConversation;
    private MyThread thread;

    private JPanel conversationPanel;
    private JPanel messageHistoirePanel;
    private JLabel conversationPseudonymeLabel;
    private JPanel inputPanel;
    private JPanel utilisateurDisponibleJPanel;
    private JLabel pseudonymeJLabel;
    private JTextField pseudonymeJTextField;
    private JScrollPane messageScrollPane;


    public MainWindow(Utilisateur mainUtilisateur) {
        this.mainUtilisateur = mainUtilisateur;
        this.controleurMainWindow = new ControleurMainWindow(this);

        this.setTitle("Clavardage");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.initialiserJFrame();

        this.thread = new MyThread(this);
        this.thread.start();

        this.controleurWindow = new ControleurWindow(this);
        this.addWindowListener(controleurWindow);
    }

    public void initialiserJFrame() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //-----------------------WEST------------------------//

        JPanel utilisateurJPanel = new JPanel(new BorderLayout());
        JLabel utilisateursJLabel = new JLabel("Online users");
        utilisateurDisponibleJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        utilisateurDisponibleJPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        updateUtilisateursConnectes();

        JScrollPane utilisateurScrollPane = new JScrollPane(utilisateurDisponibleJPanel);
        utilisateurScrollPane.setPreferredSize(new Dimension(110,580));
        utilisateurScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        utilisateurScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        utilisateurJPanel.add(utilisateursJLabel,BorderLayout.NORTH);
        utilisateurJPanel.add(utilisateurScrollPane,BorderLayout.CENTER);


        //-----------------------CENTER------------------------//
        conversationPanel = new JPanel(new BorderLayout());

        conversationPseudonymeLabel = new JLabel("");
        conversationPseudonymeLabel.setFont(new Font("Arial",Font.BOLD,15));

        messageHistoirePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        messageHistoirePanel.setPreferredSize(new Dimension(0,0));
        messageScrollPane = new JScrollPane(messageHistoirePanel);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum());

        inputPanel = new JPanel(new FlowLayout());

        conversationPanel.add(conversationPseudonymeLabel, BorderLayout.NORTH);
        conversationPanel.add(messageScrollPane, BorderLayout.CENTER);
        conversationPanel.add(inputPanel, BorderLayout.SOUTH);

        //-----------------------NORTH------------------------//
        JPanel pseudonymePanel = new JPanel(new FlowLayout());
        pseudonymeJTextField = new JTextField(20);
        pseudonymeJLabel = new JLabel(mainUtilisateur.getPseudonyme());
        pseudonymeJLabel.setLabelFor(pseudonymeJTextField);
        //((AbstractDocument) pseudonymeJTextField.getDocument()).setDocumentFilter(new CharDocumentFilter());
        JButton pseudonymeJButton = new JButton("Changer pseudonyme");

        pseudonymePanel.add(pseudonymeJLabel);
        pseudonymePanel.add(pseudonymeJTextField);
        pseudonymePanel.add(pseudonymeJButton);

        pseudonymeJButton.addActionListener(e -> this.controleurMainWindow.onChangePseudonymeButtonClicked(mainUtilisateur,pseudonymeJTextField.getText()));


        mainPanel.add(pseudonymePanel, BorderLayout.NORTH);
        mainPanel.add(utilisateurJPanel, BorderLayout.WEST);
        mainPanel.add(conversationPanel, BorderLayout.CENTER);
        this.add(mainPanel);

    }

    public void updateActiveConversation(Conversation conversation) {
        //North
        conversationPseudonymeLabel.setText(conversation.getReceiver().getPseudonyme());

        //Center
        messageHistoirePanel.removeAll();
        messageHistoirePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.activeConversation = conversation;
        int taille = 0;
        for (Quadruplet messageData : conversation.getHistorique()) {
            JTextArea message = new JTextArea();
            message.setText(messageData.getMessage());
            message.setEditable(false);
            message.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            message.setToolTipText(messageData.getHorodatage().toString());
            FontMetrics metrics = getFontMetrics(message.getFont());
            int hgt = (metrics.getHeight())*message.getText().split("\n").length;
            JPanel panelMessage;
            if(messageData.getSender().equals(mainUtilisateur.getIdentifiant())){
                message.setBackground(Color.LIGHT_GRAY);
                panelMessage = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,5));
            }
            else{
                message.setBackground(Color.WHITE);
                panelMessage = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
            }
            panelMessage.add(message);
            panelMessage.setPreferredSize(new Dimension(625, hgt+15));
            panelMessage.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            messageHistoirePanel.add(panelMessage);
            taille += hgt+25;

        }
        taille = Math.min(600,taille);
        messageHistoirePanel.setPreferredSize(new Dimension(0,taille));



        //South
        inputPanel.removeAll();
        JTextArea inputTextField = new JTextArea(5, 40);
        JScrollPane scrollPaneJTextArea = new JScrollPane(inputTextField);
        inputTextField.setLineWrap(true);
        inputTextField.setWrapStyleWord(true);
        inputTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //inputTextField.setPreferredSize(new Dimension(200, 100));
        JButton sendMessageButton = new JButton("Send");
        sendMessageButton.addActionListener(e -> this.controleurMainWindow.onSendMessageButtonClicked(this.activeConversation,
                new Quadruplet(reformateString(inputTextField.getText()), this.mainUtilisateur.getIdentifiant(),
                        this.activeConversation.getReceiver().getIdentifiant(), new Date().toString())));
        inputPanel.add(scrollPaneJTextArea);
        inputPanel.add(sendMessageButton);

        repaint(conversationPanel);
    }

    private static String reformateString (String message){
        if(message.charAt(message.length()-1) == '\n' || message.charAt(message.length()-1)== ' '){
            return reformateString(message.substring(0,message.length()-1));
        }
        return message;
    }

    public void updateUtilisateursConnectes() {
        listeConnecte = BaseDeDonnees.recupererLocalAgents();
        utilisateurDisponibleJPanel.removeAll();
        for (Utilisateur utilisateur : listeConnecte) {
            if(!utilisateur.getIdentifiant().equals(mainUtilisateur.getIdentifiant())) {
                JButton newUtilisateur = new JButton(utilisateur.getPseudonyme());
                newUtilisateur.setPreferredSize(new Dimension(85, 40));
                newUtilisateur.addActionListener(e -> this.controleurMainWindow.onUtilisateurClicked(this.mainUtilisateur, utilisateur));
                utilisateurDisponibleJPanel.add(newUtilisateur);
            }
            if(activeConversation != null){
                if(utilisateur.getIdentifiant().equals(activeConversation.getReceiver().getIdentifiant())) {
                    conversationPseudonymeLabel.setText(utilisateur.getPseudonyme());
                }
            }
        }

        utilisateurDisponibleJPanel.setPreferredSize(new Dimension(100,(listeConnecte.size()-1)*50));
        repaint(utilisateurDisponibleJPanel);
    }

    private static void repaint (Component component){
        component.setVisible(false);
        component.repaint();
        component.setVisible(true);
    }


    public ArrayList<Utilisateur> getListeConnecte() {
        return listeConnecte;
    }

    public Utilisateur getMainUtilisateur() {
        return mainUtilisateur;
    }

    public void updatePseudonyme(String pseudonyme){
        pseudonymeJLabel.setText(pseudonyme);
        pseudonymeJTextField.setText("");

    }

}
