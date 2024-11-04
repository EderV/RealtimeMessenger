package es.evm.messenger.repositories;

import es.evm.messenger.entities.ChatMessage;

import java.util.List;

public interface MessageStorageService {

    void addChatMessage(ChatMessage chatMessage);

    void updateChatMessageStatus(ChatMessage chatMessage);

    List<ChatMessage> getPendingAckMessagesBySender(String sender);

    List<ChatMessage> getUnsentMessagesByRecipient(String recipient);

}
