package pl.wm.android.poc.di_modules;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.squareup.otto.Bus;

import org.fusesource.mqtt.client.MQTT;

import pl.wm.android.poc.ChatActivity;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import pl.wm.android.poc.ChatSettings;
import pl.wm.android.poc.MqttTransport;
import pl.wm.android.poc.Transport;
import pl.wm.android.poc.WebSocketTransport;


/**
 * Created by Outbox on 20.07.13.
 */
@Module(injects = ChatActivity.class)
public class ChatDiModule {
    private Application application;
    public ChatDiModule(Application app)
    {
       this.application = app;
    }

    @Provides @Singleton public ChatSettings chatSettings()
    {
        Log.i("Chat", "Injecting Settings ");
        return new ChatSettings();
    }

    @Provides @Singleton public Bus ottoBus()
    {
        Log.i("Chat", "Injecting bus");
        return new Bus();
    }
    @Provides @Singleton public Handler sharedHandler()
    {
        Log.i("Chat", "Injecting shared Handler");
        return new Handler();
    }

    @Provides public Transport transport(Handler handler,Bus bus)
    {
        Log.i("Chat", "Injecting transport "+bus.toString());
        return new WebSocketTransport(bus, handler);
    }
}
