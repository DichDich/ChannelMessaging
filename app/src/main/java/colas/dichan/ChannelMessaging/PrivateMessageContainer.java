package colas.dichan.ChannelMessaging;

import java.util.List;

public class PrivateMessageContainer {
    private List<PrivateMessage> messages;

    public PrivateMessageContainer(List<PrivateMessage> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "MessageContainer{" +
                "messages=" + messages +
                '}';
    }

    public List<PrivateMessage> getMessages() {

        return messages;
    }

    public void setMessages(List<PrivateMessage> messages) {
        this.messages = messages;
    }
}
