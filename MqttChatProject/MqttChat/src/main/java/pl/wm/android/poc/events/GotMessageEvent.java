package pl.wm.android.poc.events;

import pl.wm.android.poc.model.ChatMessage;

/**
 * Created by Outbox on 19.07.13.
 */
public class GotMessageEvent extends MessageEvent{
    public GotMessageEvent(ChatMessage message) {
        super(message);
    }
}
