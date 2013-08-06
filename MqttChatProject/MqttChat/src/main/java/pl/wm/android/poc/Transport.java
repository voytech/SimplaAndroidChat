package pl.wm.android.poc;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


import pl.wm.android.poc.events.ConfigureDaemonEvent;
import pl.wm.android.poc.events.GotMessageEvent;
import pl.wm.android.poc.events.SendMessageEvent;
import pl.wm.android.poc.model.ChatMessage;



public abstract class Transport
{
    protected final Bus bus;

    public Transport(Bus bus)
    {
        Log.i("Chat","Transport bus hashCode"+bus.hashCode());
        this.bus = bus;
    }
    public void received(ChatMessage message)
    {
        bus.post(new GotMessageEvent(message));
    }

    public void registerSelf()
    {
        bus.register(this);
    }

    public void unregisterSelf()
    {
        bus.unregister(this);
    }


    //@TODO Why this bus subscriptions below doesn't fire ! INVESTIGATE. After You'll figure it out - just remove code below.

    @Subscribe
    public  void doConfigurationChanged(ConfigureDaemonEvent event)
    {
        Log.i("Chat","onConfigurationChanged event");
        this.onConfigurationChanged(event);
    };

    @Subscribe
    public  void doSendMessage(SendMessageEvent event)
    {
        Log.i("Chat","onSendMessage event");
        this.onSendMessage(event);
    };

    protected abstract void onConfigurationChanged(ConfigureDaemonEvent event);
    protected abstract void onSendMessage(SendMessageEvent send);

}
