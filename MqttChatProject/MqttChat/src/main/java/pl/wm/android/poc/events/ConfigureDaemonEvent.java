package pl.wm.android.poc.events;

import pl.wm.android.poc.ChatConfig;


/**
 * Created by Outbox on 19.07.13.
 */
public class ConfigureDaemonEvent {
    private ChatConfig payload;

    public ConfigureDaemonEvent(ChatConfig config)
    {
        this.payload = config;
    }

    public ChatConfig getPayload() {
        return payload;
    }

    public void setPayload(ChatConfig payload) {
        this.payload = payload;
    }

}
