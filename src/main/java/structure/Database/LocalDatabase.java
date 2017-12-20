package structure.Database;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import structure.InvalidCSVFileException;
import structure.Utilisateur;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class LocalDatabase {

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

    public static void addUtilisateurLocal(Utilisateur user)  {
        try {
            String utilisateurString = "\n"+user.getIdentifiant()+","+user.getPseudonyme()+","+user.getIPAddress()+","+user.getPort();
            Files.write(Paths.get("pseudonyme.csv"), utilisateurString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUtilisateurLocal(Utilisateur user){
        ArrayList<Utilisateur> listeConnecte = recupererLocalAgents();
        for(Utilisateur userExamine : listeConnecte){
            if (userExamine.getIdentifiant().equals(user.getIdentifiant())){
                listeConnecte.remove(userExamine);
                break;
            }
        }
        try {
            String firstLine = "Identification Pseudonyme AdresseIP Port";
            Files.write(Paths.get("pseudonyme.csv"), firstLine.getBytes());
            for(Utilisateur userExamine2 : listeConnecte){
                addUtilisateurLocal(userExamine2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Utilisateur getUtilisateur(String identifiant){
        try {
            Reader reader = Files.newBufferedReader(Paths.get("pseudonyme.csv"));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length == 4) {
                    if(nextLine[0].equals(identifiant))
                        return new Utilisateur(nextLine[0], nextLine[1],nextLine[2],Integer.valueOf(nextLine[3]));
                } else {
                    throw new InvalidCSVFileException();
                }
            }
            csvReader.close();
        } catch (Exception e) {
        }
        return null;
    }

    public static void changerPseudonyme(Utilisateur user, String pseudonyme){
        deleteUtilisateurLocal(user);
        user.setPseudonyme(pseudonyme);
        addUtilisateurLocal(user);
        LoginDatabase.updateLoginUtilisateur(user.getIdentifiant(),pseudonyme);

    }
}
