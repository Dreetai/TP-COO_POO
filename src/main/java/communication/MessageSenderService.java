package communication;

public interface MessageSenderService {
    void sendMessageOn(String ipAddress, int port, String message) throws Exception;
}
