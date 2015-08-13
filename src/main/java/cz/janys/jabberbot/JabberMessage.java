package cz.janys.jabberbot;

import org.springframework.integration.xmpp.XmppHeaders;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Martin Janys
 */
public class JabberMessage {

    private final Message<?> originalMessage;
    private final String payload;
    private final List<String> tokens;

    public JabberMessage(Message<?> originalMessage) {
        this.originalMessage = originalMessage;
        this.payload = originalMessage.getPayload() != null ? originalMessage.getPayload().toString() : "";
        this.tokens = tokens(this.payload);
    }

    private List<String> tokens(String payload) {
        List<String> tokens = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(payload);
        while(stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }
        return tokens;
    }

    public Object getHeader(String key) {
        return originalMessage.getHeaders().get(key);
    }

    public String from() {
        return (String) getHeader(XmppHeaders.FROM);
    }

    public String to() {
        return  (String) getHeader(XmppHeaders.TO);
    }

    public String type() {
        return ((org.jivesoftware.smack.packet.Message.Type)getHeader(XmppHeaders.TYPE)).name();
    }

    public Message<?> getOriginalMessage() {
        return originalMessage;
    }

    public String getPayload() {
        return payload;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public String getToken(int index) {
        return index < tokens.size() ? tokens.get(index) : null;
    }
}
