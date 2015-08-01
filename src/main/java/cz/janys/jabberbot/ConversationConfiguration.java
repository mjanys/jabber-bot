package cz.janys.jabberbot;

import java.util.concurrent.Future;

/**
 * @author Martin Janys
 */
public class ConversationConfiguration {

    private long timeout;
    private long delay;

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
