package structure;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    private static final ArrayList<Utilisateur> localContents = BaseDeDonnees.recupererLocalAgents();
    private static final ArrayList<LoginUtilisateur> loginContents = BaseDeDonnees.recupererLoginUtilisateurs();

    private BaseDeDonnees baseDeDonnees;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() throws IOException{
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        baseDeDonnees = new BaseDeDonnees();

        String firstLine = "Identifiant Password Pseudonyme";
        Files.write(Paths.get("login_information.csv"), firstLine.getBytes());
        firstLine = "Identification Pseudonyme AdresseIP Port";
        Files.write(Paths.get("pseudonyme.csv"), firstLine.getBytes());
    }

    @Test
    public void test_adding_user_in_the_database(){
        BaseDeDonnees.addLoginUtilisateur(username,password,pseudonyme);
        ArrayList<LoginUtilisateur> utilisateurArrayList = BaseDeDonnees.recupererLoginUtilisateurs();
        assertThat(utilisateurArrayList).extracting(LoginUtilisateur::getIdentifiant,
                                                    LoginUtilisateur::getMotDePasse,
                                                    LoginUtilisateur::getPseudonyme)
            .contains(tuple(username,password,pseudonyme));
    }

    @Test
    public void test_adding_local_user(){
        BaseDeDonnees.addUtilisateurLocal(testUser);
        ArrayList<Utilisateur> utilisateurArrayList = BaseDeDonnees.recupererLocalAgents();
        assertThat(utilisateurArrayList).extracting(Utilisateur::getIdentifiant,
                                                    Utilisateur::getPseudonyme,
                                                    Utilisateur::getIPAddress,
                                                    Utilisateur::getPort)
            .contains(tuple(testUser.getIdentifiant(),testUser.getPseudonyme(),testUser.getIPAddress(),testUser.getPort()));
    }

    @Test
    public void test_that_changing_pseudonyme_works_for_both_database_files(){
        BaseDeDonnees.addLoginUtilisateur(testUser.getIdentifiant(),password,testUser.getPseudonyme());
        BaseDeDonnees.addUtilisateurLocal(testUser);
        BaseDeDonnees.changerPseudonyme(testUser,"newPseudo");
        ArrayList<Utilisateur> utilisateurArrayList = BaseDeDonnees.recupererLocalAgents();
        assertThat(utilisateurArrayList).extracting(Utilisateur::getIdentifiant,
            Utilisateur::getPseudonyme,
            Utilisateur::getIPAddress,
            Utilisateur::getPort)
            .contains(tuple(testUser.getIdentifiant(),"newPseudo",testUser.getIPAddress(),testUser.getPort()));
        ArrayList<LoginUtilisateur> loginUtilisateurArrayList = BaseDeDonnees.recupererLoginUtilisateurs();
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
            BaseDeDonnees.addLoginUtilisateur(loginUser.getIdentifiant(),loginUser.getMotDePasse(),loginUser.getPseudonyme());
        }

        firstLine = "Identification Pseudonyme AdresseIP Port";
        Files.write(Paths.get("pseudonyme.csv"), firstLine.getBytes());
        for (Utilisateur user : localContents){
            BaseDeDonnees.addUtilisateurLocal(user);
        }
    }
}
