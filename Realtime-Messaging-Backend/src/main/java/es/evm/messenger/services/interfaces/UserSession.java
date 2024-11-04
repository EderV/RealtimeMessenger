package es.evm.messenger.services.interfaces;

public interface UserSession {

    void userConnected(String username);

    void userDisconnected(String username);

    boolean isUserConnected(String username);

}
