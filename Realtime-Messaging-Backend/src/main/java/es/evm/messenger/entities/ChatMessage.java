package es.evm.messenger.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@Document("messages-storage")
public class ChatMessage {

    @Id
    String id;
    String sender;
    String recipient;
    String content;
    MessageStatus status; // Status of the message in the sender
    MessageStatus statusAck; // Status of the ack provided by the recipient
    String dateTime;
    Long timeStampUpdated;

}
