package structure;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import javax.rmi.CORBA.Util;

public class BaseDeDonnees {

    public static ArrayList<Utilisateur> recupererLocalAgents() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("pseudonyme.csv"));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            ArrayList<Utilisateur> listeConnecte = new ArrayList<>();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length == 4) {
                    listeConnecte.add(new Utilisateur(nextLine[0], nextLine[1],nextLine[2],Integer.valueOf(nextLine[3])));
                } else {
                    throw new InvalidCSVFileException();
                }
            }
            csvReader.close();
            return listeConnecte;
        } catch (Exception e) {

        }
        return null;
    }


    public static ArrayList<LoginUtilisateur> recupererLoginUtilisateurs(){
        try  {
            Reader reader = Files.newBufferedReader(Paths.get("login_information.csv"));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            ArrayList<LoginUtilisateur> listeUtilisateurs = new ArrayList<>();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length == 3) {
                    listeUtilisateurs.add(new LoginUtilisateur(nextLine[0], nextLine[1],nextLine[2]));
                } else {
                    throw new InvalidCSVFileException();
                }
            }
            csvReader.close();
            return listeUtilisateurs;
        } catch (Exception e) {
    }
        return null;
    }
    public static void addUtilisateurLocal(Utilisateur user)  {
        try {
            String utilisateurString = "\n"+user.getIdentifiant()+","+user.getPseudonyme()+","+user.getIPAddress()+","+user.getPort();
            Files.write(Paths.get("pseudonyme.csv"), utilisateurString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLoginUtilisateur(String identifiant,String password,String pseudonyme) {
        String utilisateurString = "\n"+identifiant+","+password+","+pseudonyme;
        try {
            Files.write(Paths.get("login_information.csv"), utilisateurString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUtilisateurLocal(Utilisateur user){
        ArrayList<Utilisateur> listeConnecte = recupererLocalAgents();
        Utilisateur toDelete = null;
        for(Utilisateur userExamine : listeConnecte){
            if (userExamine.getIdentifiant().equals(user.getIdentifiant())){
                toDelete = userExamine;
            }
        }
        listeConnecte.remove(toDelete);
        try {
            String firstLine = "Identification Pseudonyme AdresseIP Port";
            Files.write(Paths.get("pseudonyme.csv"), firstLine.getBytes());
            for(Utilisateur userExamine2 : listeConnecte){
                String utilisateurString = "\n"+userExamine2.getIdentifiant()+","+userExamine2.getPseudonyme()+","+userExamine2.getIPAddress()+","+userExamine2.getPort();

                Files.write(Paths.get("pseudonyme.csv"), utilisateurString.getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLoginUtilisateur(String identifiant, String newPseudonyme){
        ArrayList<LoginUtilisateur> listeUtilisateur = recupererLoginUtilisateurs();
        LoginUtilisateur toDelete = null;
        for(LoginUtilisateur userExamine : listeUtilisateur){
            if (userExamine.getIdentifiant().equals(identifiant)){
                toDelete = userExamine;
            }
        }
        listeUtilisateur.remove(toDelete);
        try {
            String firstLine = "Identifiant Password Pseudonyme";
            Files.write(Paths.get("login_information.csv"), firstLine.getBytes());
            for(LoginUtilisateur userExamine2 : listeUtilisateur){
                String utilisateurString = "\n"+userExamine2.getIdentifiant()+","+userExamine2.getMotDePasse()+","+userExamine2.getPseudonyme();
                Files.write(Paths.get("login_information.csv"), utilisateurString.getBytes(), StandardOpenOption.APPEND);
            }
            String utilisateurString = "\n"+toDelete.getIdentifiant()+","+toDelete.getMotDePasse()+","+newPseudonyme;
            Files.write(Paths.get("login_information.csv"), utilisateurString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean testerSiLoginOK(String identifiant, String password){
        ArrayList<LoginUtilisateur> listeUtilisateurs = recupererLoginUtilisateurs();
        for (LoginUtilisateur user : listeUtilisateurs) {
            if (identifiant.equals(user.getIdentifiant()) && user.getMotDePasse().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static boolean testerSiAddUserOK(String identifiant){
        ArrayList<LoginUtilisateur> listeUtilisateurs = recupererLoginUtilisateurs();
        for (LoginUtilisateur user : listeUtilisateurs){
            if(identifiant.equals(user.getIdentifiant())){
                return false;
            }
        }
        return true;
    }


    // Lire/creer fichier "UTILISATEUR1_UTILISATEUR2.csv" et ajouter les messages historiques dans Conversation
    public static void recupererHistorique(Conversation conversation) {

        try (
                Reader reader = Files.newBufferedReader(Paths.get(constituerFilename(conversation)));
                CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

        ) {
            String [] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length == 4){
                    conversation.ajouterMessage(new Quadruplet(nextLine[0].replaceAll("/n","\n"),nextLine[1],nextLine[2],nextLine[3]));
                }
                else{
                    throw new InvalidCSVFileException();
                }
            }
            reader.close();
        }
        catch (Exception e){
            try{
                String path= constituerFilename(conversation);
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));
                String init = "Message Expediteur Reveceur Date";
                Files.write(Paths.get(path), init.getBytes(), StandardOpenOption.APPEND);
                writer.close();
            }catch (Exception ed){}
        }
    }

    public static void sauvegarderMessage (Conversation conversation, Quadruplet data){
        CSVWriter writer;
        String filename = constituerFilename(conversation);
        try {
            /*writer = new CSVWriter(new Files(filename));
            String[] entries =
                    (data.getMessage()+", "
                            +((Utilisateur) data.getSender()).getPseudonyme()+", "
                            +((Utilisateur) data.getReceiver()).getPseudonyme()+", "
                            +data.getHorodatage().toString()).split(",");
            writer.writeNext(entries);
            writer.close();*/
            String message = data.getMessage().replaceAll("\n","/n");
            String donnees = "\n"+message+"," +data.getSender()+","
                    +data.getReceiver()+","
                    +data.getHorodatage().toString();
            Files.write(Paths.get(filename), donnees.getBytes(), StandardOpenOption.APPEND);

        }catch(Exception e){}
    }

    // Creer le nom de la fichier dans l'ordre alphabetic
    private static String constituerFilename(Conversation conversation){
        String u1 = conversation.getSender().getIdentifiant();
        String u2 = conversation.getReceiver().getIdentifiant();
        return ((u1.compareTo(u2)<0) ? u1+"_"+u2+".csv" : u2+"_"+u1+".csv");
    }

    public static Utilisateur getUtilisateur(String identifiant){
        Utilisateur user = null;
        try {
            Reader reader = Files.newBufferedReader(Paths.get("pseudonyme.csv"));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length == 4) {
                    if(nextLine[0].equals(identifiant))
                        user = new Utilisateur(nextLine[0], nextLine[1],nextLine[2],Integer.valueOf(nextLine[3]));
                } else {
                    throw new InvalidCSVFileException();
                }
            }
            csvReader.close();
        } catch (Exception e) {

        }
        return user;
    }

    public static String getPseudonyme(String identifiant){
        ArrayList<LoginUtilisateur> loginUtilisateurArrayList = recupererLoginUtilisateurs();
        for (LoginUtilisateur user : loginUtilisateurArrayList){
            if (user.getIdentifiant().equals(identifiant)){
                return user.getPseudonyme();
            }
        }
        return null;
    }

    public static void changerPseudonyme(Utilisateur user, String pseudonyme){
        deleteUtilisateurLocal(user);
        user.setPseudonyme(pseudonyme);
        addUtilisateurLocal(user);
        updateLoginUtilisateur(user.getIdentifiant(),pseudonyme);

    }
}
