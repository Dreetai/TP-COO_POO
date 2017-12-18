package structure;

import java.io.IOException;

public class Utilisateur {
    private String identifiant;
    private String pseudonyme;
    private String ipAddress = "127.0.0.1";
    private int port;

    public Utilisateur(String identifiant){

        this.identifiant = identifiant;
        this.pseudonyme = identifiant;
        this.port = randomWithRange(4000,5000);
    }

    public Utilisateur(String identifiant, String pseudonyme){

        this.identifiant = identifiant;
        this.pseudonyme = pseudonyme;
        this.port = randomWithRange(4000,5000);
    }


    public Utilisateur(String identifiant,String pseudonyme, String ipAddress,int port){

        this.identifiant = identifiant;
        this.pseudonyme = pseudonyme;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void setPseudonyme(String pseudonyme){
        this.pseudonyme = pseudonyme;
    }

    public String getIPAddress() {
        return this.ipAddress;
    }

    public String getPseudonyme() {
        return pseudonyme;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public int getPort() {
        return port;
    }

    public static int randomWithRange(int min, int max)
    {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }


}
