package main.structure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Clavardage {

    private List <Utilisateur> pseudonymes;


    public Clavardage(Utilisateur utilisateur) throws FileNotFoundException, IOException, InvalidCSVFileException{

        this.pseudonymes = BaseDeDonnees.recupererLocalAgents();
    }

    // Pourrait changer localAgents "live", donc ajouter et supprimer les utilisateurs de la fichier quand ils
    // connectent/deconnectent et miser à jour les pseudonymes

}
