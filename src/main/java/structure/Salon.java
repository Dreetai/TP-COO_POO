package structure;

import java.util.List;

public class Salon {

    private String nom;
    private String ipAdresse;
    private int port;
    private List<Quadruplet> historique;

    public Salon(String nom, String ipAdresse){
        this.nom = nom;
        this.ipAdresse = ipAdresse;
        this.port = Utilisateur.randomWithRange(4000,5000);
        BaseDeDonnees.recupererHistoriqueSalon(this);
    }

    public void ajouterMessage(Quadruplet data){
        this.historique.add(data);
    }

    public String getNom() {
        return nom;
    }

    public String getIpAdresse() {
        return ipAdresse;
    }

    public int getPort() {
        return port;
    }

    public List<Quadruplet> getHistorique() {
        return historique;
    }
}
