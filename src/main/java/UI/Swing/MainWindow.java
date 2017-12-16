package UI.Swing;


import communication.UDPMessageSenderService;
import structure.BaseDeDonnees;
import structure.Conversation;
import structure.Quadruplet;
import structure.Utilisateur;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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
    private JPanel utilisateurPanel;
    private JLabel pseudonymeJLabel;

    public MainWindow(Utilisateur mainUtilisateur) {
        this.mainUtilisateur = mainUtilisateur;
        this.controleurMainWindow = new ControleurMainWindow(this);

        this.setTitle("Clavardage");
        this.setSize(600, 400);
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

        JPanel utilisateur_labelPanel = new JPanel(new BorderLayout());
        JLabel utilisateursConnectesLabel = new JLabel("Online users");
        utilisateurPanel = new JPanel(new FlowLayout());
        utilisateurPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        updateUtilisateursConnectes();

        JScrollPane utilisateurScrollPane = new JScrollPane(utilisateurPanel);
        utilisateurScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        utilisateur_labelPanel.add(utilisateursConnectesLabel,BorderLayout.NORTH);
        utilisateur_labelPanel.add(utilisateurScrollPane,BorderLayout.CENTER);

        conversationPanel = new JPanel(new BorderLayout());

        conversationPseudonymeLabel = new JLabel("");
        conversationPseudonymeLabel.setFont(new Font("Arial",Font.BOLD,15));

        messageHistoirePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));

        JScrollPane messageScrollPane = new JScrollPane(messageHistoirePanel);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        inputPanel = new JPanel(new FlowLayout());

        conversationPanel.add(conversationPseudonymeLabel, BorderLayout.NORTH);
        conversationPanel.add(messageScrollPane, BorderLayout.CENTER);
        conversationPanel.add(inputPanel, BorderLayout.SOUTH);

        JPanel pseudonymePanel = new JPanel(new FlowLayout());
        JTextField pseudonymeJTextField = new JTextField(20);
        pseudonymeJLabel = new JLabel(mainUtilisateur.getPseudonyme());
        pseudonymeJLabel.setLabelFor(pseudonymeJTextField);
        ((AbstractDocument) pseudonymeJTextField.getDocument()).setDocumentFilter(new CharDocumentFilter());
        JButton pseudonymeJButton = new JButton("Change pseudonyme");


        pseudonymePanel.add(pseudonymeJLabel);
        pseudonymePanel.add(pseudonymeJTextField);
        pseudonymePanel.add(pseudonymeJButton);

        pseudonymeJButton.addActionListener(e -> this.controleurMainWindow.onChangePseudonymeButtonClicked(mainUtilisateur,pseudonymeJTextField.getText()));

        this.add(mainPanel);
        mainPanel.add(pseudonymePanel, BorderLayout.NORTH);
        mainPanel.add(utilisateur_labelPanel, BorderLayout.WEST);
        mainPanel.add(conversationPanel, BorderLayout.CENTER);

    }

    public void initialiserConversation(Conversation conversation) {
        //North
        conversationPseudonymeLabel.setText(conversation.getReceiver().getPseudonyme());

        //Center
        messageHistoirePanel.removeAll();
        messageHistoirePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.activeConversation = conversation;
        int taille = messageHistoirePanel.getWidth();
        for (Quadruplet messageData : conversation.getHistorique()) {
            JTextArea message = new JTextArea();
            message.setText(messageData.getMessage());
            message.setEditable(false);
            message.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            //message.setPreferredSize(new Dimension(300, 50));
            message.setToolTipText(messageData.getHorodatage().toString());
            System.out.println(messageHistoirePanel.getWidth());
            JPanel panelMessage;
            if(messageData.getSender().equals(mainUtilisateur.getIdentifiant())){
                message.setBackground(Color.LIGHT_GRAY);
                panelMessage = new JPanel(new FlowLayout(FlowLayout.RIGHT,15,5));
                panelMessage.add(message);
                panelMessage.setPreferredSize(new Dimension(messageHistoirePanel.getWidth(),50));
                panelMessage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
            else{
                message.setBackground(Color.WHITE);
                panelMessage = new JPanel(new FlowLayout(FlowLayout.LEFT,15,5));
                panelMessage.add(message);
                panelMessage.setPreferredSize(new Dimension(messageHistoirePanel.getWidth(),50));
                panelMessage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
            messageHistoirePanel.add(panelMessage);
            taille += 50;
        }
        messageHistoirePanel.setPreferredSize(new Dimension(taille,300));

        //South
        inputPanel.removeAll();
        JTextArea inputTextField = new JTextArea(5, 20);
        inputTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //inputTextField.setPreferredSize(new Dimension(200, 100));
        JButton sendMessageButton = new JButton("Send");
        sendMessageButton.addActionListener(e -> this.controleurMainWindow.onSendMessageButtonClicked(this.activeConversation,
                new Quadruplet(inputTextField.getText(), this.mainUtilisateur.getIdentifiant(),
                        this.activeConversation.getReceiver().getIdentifiant(), new Date().toString())));
        inputPanel.add(inputTextField);
        inputPanel.add(sendMessageButton);

        repaint(conversationPanel);

    }

    public void updateActiveConversation(Quadruplet messageData) {
        JTextArea message = new JTextArea(5, 20);
        message.setText(messageData.getMessage());
        message.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //message.setPreferredSize(new Dimension(20, 20));
        message.setEditable(false);
        if(messageData.getSender().equals(mainUtilisateur.getIdentifiant())){
            message.setBackground(Color.LIGHT_GRAY);
        }
        else
            message.setBackground(Color.WHITE);
        message.setToolTipText(messageData.getHorodatage().toString());
        messageHistoirePanel.setPreferredSize(new Dimension(300,messageHistoirePanel.getHeight()+50));
        messageHistoirePanel.add(message);
        repaint(conversationPanel);
    }

    public void updateUtilisateursConnectes() {
        listeConnecte = BaseDeDonnees.recupererLocalAgents();
        utilisateurPanel.removeAll();
        for (Utilisateur utilisateur : listeConnecte) {
            if(!utilisateur.getPseudonyme().equals(mainUtilisateur.getPseudonyme())) {
                JButton newUtilisateur = new JButton(utilisateur.getPseudonyme());
                newUtilisateur.setPreferredSize(new Dimension(80, 40));
                newUtilisateur.addActionListener(e -> this.controleurMainWindow.onUtilisateurClicked(this.mainUtilisateur, utilisateur));
                utilisateurPanel.add(newUtilisateur);
            }
        }
        utilisateurPanel.setPreferredSize(new Dimension(120,(listeConnecte.size()-1)*50));
        repaint(utilisateurPanel);
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

    public void updatePseudonymeLabel(String pseudonyme){
        pseudonymeJLabel.setText(pseudonyme);

    }
}
