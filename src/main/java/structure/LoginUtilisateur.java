package structure;

public class LoginUtilisateur {

    private String identifiant;
    private String motDePasse;
    private String pseudonyme;

    public LoginUtilisateur(String identifiant, String motDePasse, String pseudonyme){
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.pseudonyme = pseudonyme;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getPseudonyme() {
        return pseudonyme;
    }
}
