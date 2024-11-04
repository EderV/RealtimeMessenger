package es.evm.messenger.config;

import es.evm.messenger.services.interfaces.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserSession userSession;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = sha.getUser();
        if (principal == null) {
            log.warn("Principal not found in SessionConnectedEvent.");
            return;
        }

        this.userSession.userConnected(principal.getName());

        log.error("Connection established: {}", sha);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = sha.getUser();
        if (principal == null) {
            log.warn("Principal not found in SessionDisconnectEvent.");
            return;
        }

        this.userSession.userDisconnected(principal.getName());

        log.error("Client disconnected: {}", sha);
    }

}
