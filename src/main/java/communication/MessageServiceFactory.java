package communication;

public interface MessageServiceFactory<T> {

    T onTCP();

    T onUDP();
}