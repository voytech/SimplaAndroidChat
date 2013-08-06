package pl.wm.android.poc.events;

import pl.wm.android.poc.model.ChatMessage;

/**
 * Created by Outbox on 19.07.13.
 */
public class MessageEvent {
    private ChatMessage message;
    public MessageEvent(ChatMessage message)
    {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

}
