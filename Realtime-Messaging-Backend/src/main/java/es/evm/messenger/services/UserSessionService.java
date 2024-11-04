package es.evm.messenger.services;

import es.evm.messenger.services.interfaces.UserSession;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService implements UserSession {

    private final Set<String> connectedUsers = ConcurrentHashMap.newKeySet();

    @Override
    public void userConnected(String username) {
        connectedUsers.add(username);
    }

    @Override
    public void userDisconnected(String username) {
        connectedUsers.remove(username);
    }

    @Override
    public boolean isUserConnected(String username) {
        return connectedUsers.contains(username);
    }

}
