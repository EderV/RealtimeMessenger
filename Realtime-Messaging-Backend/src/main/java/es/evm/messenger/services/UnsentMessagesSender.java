package es.evm.messenger.services;

import es.evm.messenger.services.interfaces.AsyncMessageSender;
import es.evm.messenger.services.interfaces.MessengerService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnsentMessagesSender implements AsyncMessageSender {

    private final MessengerService messengerService;

    private final ExecutorService executor = Executors.newFixedThreadPool(100);

    @Override
    public void start(String user) {
        executor.submit(() -> {
            this.messengerService.sendUnsentMessages(user);
            this.messengerService.sendPendingAckMessages(user);
        });
    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }

}
