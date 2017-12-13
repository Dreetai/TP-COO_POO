package main.communication;

public class MessageReceiverServiceFactory implements MessageServiceFactory<MessageReceiverService> {
    @Override
    public MessageReceiverService onTCP() {

            return new TCPMessageReceiverService();

    }

    @Override
    public MessageReceiverService onUDP() {
        return new UDPMessageReceiverService();
    }
}