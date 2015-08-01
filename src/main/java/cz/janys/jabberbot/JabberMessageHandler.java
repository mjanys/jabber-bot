package cz.janys.jabberbot;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * @author Martin Janys
 */
@Component
public abstract class JabberMessageHandler {

    public abstract boolean canHandle(JabberMessage message);

    public abstract Message<?> doHandleMessage(JabberMessage message, ConversationScope<String, Object> conversationScope) throws MessagingException;

    public abstract String getConversationId();

    public Message<?> handleJabberMessage(JabberMessage message, ConversationScope<String, Object> conversationScope) throws MessagingException {
        return doHandleMessage(message, conversationScope);
    }

    public JabberMessage parseMessage(Message<?> originalMessage) {
        return new JabberMessage(originalMessage);
    }

}
