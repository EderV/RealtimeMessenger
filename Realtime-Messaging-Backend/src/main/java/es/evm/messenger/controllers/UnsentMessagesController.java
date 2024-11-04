package es.evm.messenger.controllers;

import es.evm.messenger.entities.ChatMessage;
import es.evm.messenger.repositories.MessageStorageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/unsent")
@RequiredArgsConstructor
public class UnsentMessagesController {

    private final MessageStorageServiceImpl unsentMessagesService;

//    @GetMapping
//    public ResponseEntity<List<ChatMessage>> getUnsentMessages(String recipient) {
//        var unsentMessages = unsentMessagesService.readMessagesByRecipient(recipient);
//        unsentMessagesService.deleteUnsentMessages(recipient);
//        return ResponseEntity.ok(unsentMessages);
//    }

}
