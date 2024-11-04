package es.evm.messenger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.evm.messenger.entities.ChatMessage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.MimeTypeUtils;

@Slf4j
public class InboundMessageInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            log.error("No StompHeaderAccessor found");
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            this.handleConnectCommand(accessor);
        }
        else if (StompCommand.SEND.equals(accessor.getCommand()) &&
                SimpMessageType.MESSAGE.equals(accessor.getMessageType())
        ) {
//            this.handleMessageCommand(accessor, message); // TODO implement message validation
        }

        return message;
    }

    private void handleConnectCommand(StompHeaderAccessor accessor) throws IllegalArgumentException {
        String user = accessor.getFirstNativeHeader("login");
        String pass = accessor.getFirstNativeHeader("passcode");

        // TODO authenticate user

        log.info("User login received: {}", user);

        accessor.setUser(() -> user);

        // Reject the connection if user is not valid
//            throw new IllegalArgumentException("Invalid credentials");
    }

    private void handleMessageCommand(
            @NonNull StompHeaderAccessor accessor,
            @NonNull Message<?> message
    ) throws IllegalArgumentException {
        var destination = accessor.getDestination();
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination is null or empty");
        }

        if (accessor.getDestination().equals("/app/chat")) {
            DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
            resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
            MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
            converter.setContentTypeResolver(resolver);

            StringMessageConverter byteArrayMessageConverter = new StringMessageConverter();

            ChatMessage chatMessagee = (ChatMessage) byteArrayMessageConverter.fromMessage(message, ChatMessage.class);

//            ChatMessage chatMessage = (ChatMessage) message.getPayload();
            String chatMessage = String.valueOf(message.getPayload());

            var asdgasd = "lkjh";

//            var sender = chatMessage.getSender();
//            var recipient = chatMessage.getRecipient();
//            if (sender != null && !sender.isBlank() && recipient != null && !recipient.isBlank()) {
//                return;
//            }
//
//            throw new IllegalArgumentException("Chat message has no sender or recipient. Message: " + chatMessage);
        }
    }
}
