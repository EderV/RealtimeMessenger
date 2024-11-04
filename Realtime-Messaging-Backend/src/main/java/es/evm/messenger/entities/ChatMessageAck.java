package es.evm.messenger.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChatMessageAck {

    String messageId;
    String sender;
    String recipient;
    MessageStatus status;

}
