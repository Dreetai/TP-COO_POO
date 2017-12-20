package structure;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    private BaseDeDonnees baseDeDonnees;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        baseDeDonnees = new BaseDeDonnees();
    }

    @Test
    public void test_adding_user_in_the_database(){
        BaseDeDonnees.addLoginUtilisateur(username,password);
        ArrayList<LoginUtilisateur> utilisateurArrayList = BaseDeDonnees.recupererLoginUtilisateurs();
        assertThat(utilisateurArrayList).extracting(LoginUtilisateur::getIdentifiant,
                                                    LoginUtilisateur::getMotDePasse,
                                                    LoginUtilisateur::getPseudonyme)
            .contains(tuple(username,password,username));
    }


    @After
    public void clearLoginCSVFile()throws IOException{
        String firstLine = "Identifiant Password Pseudonyme";
        Files.write(Paths.get("login_information.csv"), firstLine.getBytes());
    }
}
