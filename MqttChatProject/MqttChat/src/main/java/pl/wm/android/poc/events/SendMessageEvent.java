package pl.wm.android.poc.events;

import pl.wm.android.poc.model.ChatMessage;

/**
 * Created by Outbox on 19.07.13.
 */
public class SendMessageEvent extends MessageEvent{
    public SendMessageEvent(ChatMessage message) {
        super(message);
    }
}
