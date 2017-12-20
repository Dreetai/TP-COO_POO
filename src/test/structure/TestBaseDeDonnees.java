package structure;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public class TestBaseDeDonnees {

    private static final LoginUtilisateur testLoginUtilisateur = new LoginUtilisateur("username","password","pseudonyme");

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
        BaseDeDonnees.addLoginUtilisateur(testLoginUtilisateur);
        ArrayList<LoginUtilisateur> utilisateurArrayList = BaseDeDonnees.recupererLoginUtilisateurs();
        assertThat(utilisateurArrayList).contains(testLoginUtilisateur);

    }
}
