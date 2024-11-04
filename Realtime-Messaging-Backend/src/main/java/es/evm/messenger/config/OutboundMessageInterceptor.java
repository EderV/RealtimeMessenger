package es.evm.messenger.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
public class OutboundMessageInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            log.debug("No StompHeaderAccessor found");
            return message;
        }

        log.debug("Pre-send headers: {}", accessor);

//        if (StompCommand.CONNECTED.equals(accessor.getCommand())) {
//            return MessageBuilder
//                    .withPayload(message.getPayload())
//                    .copyHeaders(message.getHeaders())
//                    .setHeader("unsent-messages", "{\"message1\":\"content1\"}")
//                    .build();
//        }

        return message;
    }

}
