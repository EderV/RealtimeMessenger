package es.evm.messenger.entities;

public enum MessageStatus {

    /**
     * Default state
     */
    NONE,

    /**
     * The client sent the message and the server received it
     */
    SENT,

    /**
     * The server sent the message to the recipient
     */
    DELIVERED,

    /**
     * The recipient read the message
     */
    READ

}
