package main.communication;

public class MessageSenderServiceFactory implements MessageServiceFactory<MessageSenderService> {

    @Override
    public MessageSenderService onTCP() {
        return new TCPMessageSenderService();
    }

    @Override
    public MessageSenderService onUDP() {
        return new UDPMessageSenderService();
    }
}