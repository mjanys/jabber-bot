package cz.janys.jabberbot.task;

import cz.janys.jabberbot.ConversationScope;

/**
 * @author Martin Janys
 */
public class ClearConversation<K, V> implements Runnable {

    private final ConversationScope<K, V> conversationScope;

    public ClearConversation(ConversationScope<K, V> conversationScope) {
        this.conversationScope = conversationScope;
    }

    public void run() {
        conversationScope.clear();
    }
}
