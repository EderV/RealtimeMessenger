package es.evm.messenger.repositories;

import es.evm.messenger.entities.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageStorageServiceImpl implements MessageStorageService {

    private final MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public void addChatMessage(ChatMessage chatMessage) {
        mongoTemplate.save(chatMessage);
    }

    @Override
    @Transactional
    public void updateChatMessageStatus(ChatMessage chatMessage) {
        Query query = new Query(Criteria.where("id").is(chatMessage.getId()));

        Update update = new Update();
        update.set("statusAck", chatMessage.getStatusAck());

        if (chatMessage.getStatus() != null) {
            update.set("status", chatMessage.getStatus());
        }

        this.mongoTemplate.updateFirst(query, update, ChatMessage.class);
    }

    @Override
    public List<ChatMessage> getPendingAckMessagesBySender(String sender) {
        Criteria criteria = Criteria.where("sender").is(sender)
                .andOperator(Criteria.expr(
                        ComparisonOperators.Ne.valueOf(
                                "$status").notEqualTo("$statusAck")
                        )
                );

        Query query = new Query(criteria);

        return mongoTemplate.find(query, ChatMessage.class);
    }

    @Override
    public List<ChatMessage> getUnsentMessagesByRecipient(String recipient) {
        Criteria criteria = Criteria.where("recipient").is(recipient)
                .and("status").is("SENT")
                .and("statusAck").is("SENT");

        Query query = new Query(criteria);

        return this.mongoTemplate.find(query, ChatMessage.class);
    }

}
