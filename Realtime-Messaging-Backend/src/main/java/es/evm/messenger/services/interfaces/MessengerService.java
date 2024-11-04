package es.evm.messenger.services.interfaces;

import es.evm.messenger.entities.ChatMessage;
import es.evm.messenger.entities.ChatMessageAck;

import java.security.Principal;

public interface MessengerService {

    void handleChatMessage(ChatMessage chatMessage, Principal principal);

    void handleAck(ChatMessageAck chatMessageAck);

    void sendUnsentMessages(String user);

    void sendPendingAckMessages(String user);

}
