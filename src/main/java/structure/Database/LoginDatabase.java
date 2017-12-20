package structure.Database;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import structure.InvalidCSVFileException;
import structure.LoginUtilisateur;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class LoginDatabase {

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

    public static void addLoginUtilisateur(String identifiant,String password,String pseudonyme) {
        String utilisateurString = "\n"+identifiant+","+password+","+pseudonyme;
        try {
            Files.write(Paths.get("login_information.csv"), utilisateurString.getBytes(), StandardOpenOption.APPEND);
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
                listeUtilisateur.remove(toDelete);
            }
        }
        try {
            String firstLine = "Identifiant Password Pseudonyme";
            Files.write(Paths.get("login_information.csv"), firstLine.getBytes());
            for(LoginUtilisateur userExamine2 : listeUtilisateur){
                addLoginUtilisateur(userExamine2.getIdentifiant(),userExamine2.getMotDePasse(),userExamine2.getPseudonyme());
            }
            addLoginUtilisateur(toDelete.getIdentifiant(),toDelete.getMotDePasse(),newPseudonyme);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
