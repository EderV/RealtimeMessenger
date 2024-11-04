package es.evm.messenger.services;

import es.evm.messenger.entities.ChatMessage;
import es.evm.messenger.entities.ChatMessageAck;
import es.evm.messenger.entities.MessageStatus;
import es.evm.messenger.repositories.MessageStorageService;
import es.evm.messenger.services.interfaces.MessengerService;
import es.evm.messenger.services.interfaces.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessengerServiceDefault implements MessengerService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserSession userSession;
    private final MessageStorageService messageStorageService;

    @Override
    public void handleChatMessage(ChatMessage chatMessage, Principal principal) {
        var recipient = chatMessage.getRecipient();
        var sender = principal.getName();

        chatMessage.setStatus(MessageStatus.SENT);
        chatMessage.setStatusAck(MessageStatus.SENT);

        var messageAck = ChatMessageAck.builder()
                .messageId(chatMessage.getId())
                .sender(sender)
                .recipient(recipient)
                .status(chatMessage.getStatus())
                .build();

        this.messageStorageService.addChatMessage(chatMessage);

        var des = "/queue/ack.".concat(sender);
        this.messagingTemplate.convertAndSend(des, messageAck);

        if (this.userSession.isUserConnected(recipient)) {
            var dest = "/queue/".concat(recipient);
            this.messagingTemplate.convertAndSend(dest, chatMessage);
        }
    }

    @Override
    public void handleAck(ChatMessageAck chatMessageAck) {
        var recipient = chatMessageAck.getRecipient(); // The one who received the message
        var sender = chatMessageAck.getSender(); // The one who sent the message

        var chatMessage = ChatMessage.builder()
                .id(chatMessageAck.getMessageId())
                .statusAck(chatMessageAck.getStatus())
                .build();

        if (this.userSession.isUserConnected(sender)) {
            chatMessage.setStatus(chatMessageAck.getStatus());

            var dest = "/queue/ack.".concat(recipient);
            this.messagingTemplate.convertAndSend(dest, chatMessageAck);
        }

        this.messageStorageService.updateChatMessageStatus(chatMessage);
    }

    @Async
    @Override
    public void sendUnsentMessages(String user) {
        var unsentMessages = this.messageStorageService.getUnsentMessagesByRecipient(user);

        for (var unsentMessage : unsentMessages) {
            if (!this.userSession.isUserConnected(user)) {
                break;
            }

            this.messagingTemplate.convertAndSend("/queue/".concat(user), unsentMessage);

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                log.debug("Unsent messages interrupted. Message: {}", e.getMessage());
            }
        }
    }

    @Async
    @Override
    public void sendPendingAckMessages(String user) {
        var pendingAckMessages = this.messageStorageService.getPendingAckMessagesBySender(user);

        for (var pendingAckMessage : pendingAckMessages) {
            if (!this.userSession.isUserConnected(user)) {
                break;
            }

            this.messagingTemplate.convertAndSend("/queue/ack.".concat(user), pendingAckMessage);

            // Update the status with the statusAck because now the sender knows the new statusAck
            pendingAckMessage.setStatus(pendingAckMessage.getStatusAck());
            this.messageStorageService.updateChatMessageStatus(pendingAckMessage);

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                log.debug("Pending ack interrupted. Message: {}", e.getMessage());
            }
        }
    }

}
