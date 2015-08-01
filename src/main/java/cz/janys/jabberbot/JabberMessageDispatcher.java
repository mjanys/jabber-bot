package cz.janys.jabberbot;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.xmpp.XmppHeaders;
import org.springframework.messaging.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Janys
 */
@Component
@Lazy(false)
@SuppressWarnings("unchecked")
public class JabberMessageDispatcher implements MessageHandler, InitializingBean {

    @Autowired(required = false)
    private List<JabberMessageHandler> handlers;

    @Autowired(required = false)
    private DefaultJabberMessageHandler defaultHandler;

    @Autowired(required = false)
    private Map<String, ConversationConfiguration> configurations;

    @Qualifier("inboundChannel")
    @Autowired
    private SubscribableChannel subscribableChannel;

    @Qualifier("outboundChannel")
    @Autowired
    private MessageChannel outboundChannel;

    private Map<String, ConversationScope<String, Object>> conversations;

    protected JabberMessageDispatcher() {
        conversations = Collections.synchronizedMap(new HashMap<String, ConversationScope<String, Object>>());
    }

    public void afterPropertiesSet() throws Exception {
        subscribableChannel.subscribe(this);
        if (defaultHandler != null) {
            handlers.remove(defaultHandler);
            handlers.add(defaultHandler); // last handler
        }
    }

    public synchronized void handleMessage(Message<?> originalMessage) throws MessagingException {
        if (!CollectionUtils.isEmpty(handlers)) {
            for (JabberMessageHandler handler : handlers) {
                JabberMessage message = handler.parseMessage(originalMessage);
                if (handler.canHandle(message)) {
                    Message<?> reply = handler.handleJabberMessage(message, getConversationScope(handler.getConversationId()));
                    if (reply != null) {
                        Message<?> response = MessageBuilder
                                .fromMessage(reply)
                                .setHeader(XmppHeaders.TO, originalMessage.getHeaders().get(XmppHeaders.FROM))
                                .setReplyChannel(outboundChannel)
                                .build();
                        outboundChannel.send(response);
                    }
                    return;
                }
            }
        }
    }

    private ConversationScope<String, Object> getConversationScope(String conversationId) {
        if (!conversations.containsKey(conversationId)) {
            ConversationScope<String, Object> conversation = ConversationScope.create(configurations.get(conversationId));
            conversations.put(conversationId, conversation);
        }
        return conversations.get(conversationId);
    }

    public Map<String, Object> getConversationMap(String conversationId) {
        return Collections.unmodifiableMap(getConversationScope(conversationId));
    }
}
