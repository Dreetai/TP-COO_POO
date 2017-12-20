package structure;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import structure.Database.LocalDatabase;
import structure.Database.LoginDatabase;

import javax.rmi.CORBA.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public class TestBaseDeDonnees {

    private static final String username = "testperson";
    private static final String password = "testpassword";
    private static final String pseudonyme = "testpseudonyme";
    private static final Utilisateur testUser = new Utilisateur(username,pseudonyme);

    private static final ArrayList<Utilisateur> localContents = LocalDatabase.recupererLocalAgents();
    private static final ArrayList<LoginUtilisateur> loginContents = LoginDatabase.recupererLoginUtilisateurs();

    @Before
    public void setUp() throws IOException{
        String firstLine = "Identifiant Password Pseudonyme";
        Files.write(Paths.get("login_information.csv"), firstLine.getBytes());
        firstLine = "Identification Pseudonyme AdresseIP Port";
        Files.write(Paths.get("pseudonyme.csv"), firstLine.getBytes());
    }

    @Test
    public void test_adding_user_in_the_database(){
        LoginDatabase.addLoginUtilisateur(username,password,pseudonyme);
        ArrayList<LoginUtilisateur> utilisateurArrayList = LoginDatabase.recupererLoginUtilisateurs();
        assertThat(utilisateurArrayList).extracting(LoginUtilisateur::getIdentifiant,
                                                    LoginUtilisateur::getMotDePasse,
                                                    LoginUtilisateur::getPseudonyme)
            .contains(tuple(username,password,pseudonyme));
    }

    @Test
    public void test_adding_and_removing_local_user(){
        LocalDatabase.addUtilisateurLocal(testUser);
        ArrayList<Utilisateur> utilisateurArrayList = LocalDatabase.recupererLocalAgents();
        assertThat(utilisateurArrayList).extracting(Utilisateur::getIdentifiant,
                                                    Utilisateur::getPseudonyme,
                                                    Utilisateur::getIPAddress,
                                                    Utilisateur::getPort)
            .contains(tuple(testUser.getIdentifiant(),testUser.getPseudonyme(),testUser.getIPAddress(),testUser.getPort()));
        LocalDatabase.deleteUtilisateurLocal(testUser);
        assertThat(LocalDatabase.recupererLocalAgents()).isEmpty();
    }

    @Test
    public void test_that_changing_pseudonyme_works_for_both_database_files(){
        LoginDatabase.addLoginUtilisateur(testUser.getIdentifiant(),password,testUser.getPseudonyme());
        LocalDatabase.addUtilisateurLocal(testUser);
        LocalDatabase.changerPseudonyme(testUser,"newPseudo");
        ArrayList<Utilisateur> utilisateurArrayList = LocalDatabase.recupererLocalAgents();
        assertThat(utilisateurArrayList).extracting(Utilisateur::getIdentifiant,
            Utilisateur::getPseudonyme,
            Utilisateur::getIPAddress,
            Utilisateur::getPort)
            .contains(tuple(testUser.getIdentifiant(),"newPseudo",testUser.getIPAddress(),testUser.getPort()));
        ArrayList<LoginUtilisateur> loginUtilisateurArrayList = LoginDatabase.recupererLoginUtilisateurs();
        assertThat(loginUtilisateurArrayList).extracting(LoginUtilisateur::getIdentifiant,
            LoginUtilisateur::getMotDePasse,
            LoginUtilisateur::getPseudonyme)
            .contains(tuple(testUser.getIdentifiant(),password,"newPseudo"));
    }

    @After
    public void clearCSVFiles()throws IOException{
        String firstLine = "Identifiant Password Pseudonyme";
        Files.write(Paths.get("login_information.csv"), firstLine.getBytes());
        for (LoginUtilisateur loginUser : loginContents){
            LoginDatabase.addLoginUtilisateur(loginUser.getIdentifiant(),loginUser.getMotDePasse(),loginUser.getPseudonyme());
        }

        firstLine = "Identification Pseudonyme AdresseIP Port";
        Files.write(Paths.get("pseudonyme.csv"), firstLine.getBytes());
        for (Utilisateur user : localContents){
            LocalDatabase.addUtilisateurLocal(user);
        }
    }
}
