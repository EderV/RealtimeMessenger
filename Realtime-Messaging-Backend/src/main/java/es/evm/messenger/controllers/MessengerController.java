package es.evm.messenger.controllers;

import es.evm.messenger.entities.ChatMessage;
import es.evm.messenger.entities.ChatMessageAck;
import es.evm.messenger.services.interfaces.AsyncMessageSender;
import es.evm.messenger.services.interfaces.MessengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@MessageMapping("/chat")
@RequiredArgsConstructor
public class MessengerController {

    private final MessengerService messengerService;
    private final AsyncMessageSender asyncMessageSender;

    @MessageMapping
    public void handleChatMessage(@Payload ChatMessage chatMessage, Principal principal) {
        log.info("Payload received: {}", chatMessage);
        log.info("Principal received: {}", principal.getName());

        this.messengerService.handleChatMessage(chatMessage, principal);
    }

    @MessageMapping("/ack")
    public void handleAck(@Payload ChatMessageAck chatMessageAck, Principal principal) {
        log.info("Payload ack received: {}", chatMessageAck);
        log.info("Principal ack received: {}", principal.getName());

        this.messengerService.handleAck(chatMessageAck);
    }

    @MessageMapping("/ready")
    public void handleClientReady(Principal principal) {
        log.info("Client {} ready", principal.getName());
        var user = principal.getName();

        this.asyncMessageSender.start(user);
    }

}
