package structure;


public class Quadruplet{

    private final String message;
    private final String sender;
    private final String receiver;
    private final String horodatage;

    public Quadruplet(String message, String sender, String receiver, String horodatage) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.horodatage = horodatage;
    }

    public String getMessage() { return message; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getHorodatage() { return horodatage; }

}