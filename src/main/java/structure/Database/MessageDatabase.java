package structure.Database;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import structure.Conversation;
import structure.InvalidCSVFileException;
import structure.Quadruplet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MessageDatabase {

    public static void recupererHistorique(Conversation conversation) {

        try (
            Reader reader = Files.newBufferedReader(Paths.get(constituerFilename(conversation)));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

        ) {
            String [] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length == 4){
                    conversation.ajouterMessage(new Quadruplet(nextLine[0].replaceAll("/n","\n"),nextLine[1],nextLine[2],nextLine[3]));
                }
                else{
                    throw new InvalidCSVFileException();
                }
            }
            reader.close();
        }
        catch (Exception e){
            try{
                String path= constituerFilename(conversation);
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));
                String init = "Message Expediteur Reveceur Date";
                Files.write(Paths.get(path), init.getBytes(), StandardOpenOption.APPEND);
                writer.close();
            }catch (Exception ed){}
        }
    }

    public static void sauvegarderMessage (Conversation conversation, Quadruplet data){
        CSVWriter writer;
        String filename = constituerFilename(conversation);
        try {
            /*writer = new CSVWriter(new Files(filename));
            String[] entries =
                    (data.getMessage()+", "
                            +((Utilisateur) data.getSender()).getPseudonyme()+", "
                            +((Utilisateur) data.getReceiver()).getPseudonyme()+", "
                            +data.getHorodatage().toString()).split(",");
            writer.writeNext(entries);
            writer.close();*/
            String message = data.getMessage().replaceAll("\n","/n");
            String donnees = "\n"+message+"," +data.getSender()+","
                +data.getReceiver()+","
                +data.getHorodatage().toString();
            Files.write(Paths.get(filename), donnees.getBytes(), StandardOpenOption.APPEND);

        }catch(Exception e){}
    }

    private static String constituerFilename(Conversation conversation){
        String u1 = conversation.getSender().getIdentifiant();
        String u2 = conversation.getReceiver().getIdentifiant();
        return ((u1.compareTo(u2)<0) ? u1+"_"+u2+".csv" : u2+"_"+u1+".csv");
    }
}
