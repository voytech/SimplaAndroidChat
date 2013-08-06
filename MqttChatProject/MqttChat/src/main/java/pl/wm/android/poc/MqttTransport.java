package pl.wm.android.poc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.Lazy;
import pl.wm.android.poc.events.ConfigureDaemonEvent;
import pl.wm.android.poc.events.ConnectionFailureEvent;
import pl.wm.android.poc.events.GotMessageEvent;
import pl.wm.android.poc.events.SendMessageEvent;
import pl.wm.android.poc.model.ChatMessage;

/**
 * Created by Outbox on 18.07.13.
 */
public class MqttTransport extends Transport{


    private final MQTT mqtt = new MQTT();
    private final Handler sharedHandler;

    private FutureConnection connection;
    private ChatConfig config;
    private volatile boolean unlocked = true;


    public MqttTransport(Bus bus,Handler handler) {
       super(bus);
       this.sharedHandler = handler;
    }

    private void initialize(ChatConfig config)
    {
        Log.i("Chat", "Configuring chat " + config.getClientId() + "," + config.getHost());
        this.config = config;
        mqtt.setClientId(config.getClientId());
        try {
            mqtt.setHost(config.getHost());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void connect(final Callback<Void> after)
    {
        connection = mqtt.futureConnection();
        connection.connect().then(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                after.onSuccess(aVoid);
            }

            @Override
            public void onFailure(Throwable throwable) {
                bus.post(new ConnectionFailureEvent(throwable));
            }
        });
    }
    private void safeSendMessage(final ChatMessage message)
    {
        if (connection==null || !connection.isConnected())
        {
            connect(new Callback<Void>(){
                @Override
                public void onSuccess(Void aVoid) {
                    sendMessage(message);
                }

                @Override
                public void onFailure(Throwable throwable){

                }
            });
        }
        else
            sendMessage(message);
    }

    private void sendMessage(final ChatMessage message)
    {
        if (connection.isConnected())
        {
            connection.publish(config.getTopic(), (message.getAuthor()+":"+message.getMessage()+":"+message.getDate().getTime()).getBytes(), QoS.AT_LEAST_ONCE, false).then(new Callback<Void>()
            {
                  @Override
                  public void onSuccess(Void aVoid) {

                  }

                  @Override
                  public void onFailure(final Throwable throwable) {

                      sharedHandler.post(new Runnable() {
                          @Override
                          public void run() {
                              bus.post(new ConnectionFailureEvent(throwable));
                          }
                      });
                  }
            });
        }
    }

    private void start()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (unlocked)
                {
                    unlocked = false;
                    connection.subscribe(new Topic[]{new Topic(config.getTopic(),QoS.AT_LEAST_ONCE)}).then(new Callback<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Log.i("Chat", " subscribed... ");
                            connection.receive().then(new Callback<Message>()
                            {
                                @Override
                                public void onSuccess(final Message message) {
                                    Log.i("Chat", " message received... ");
                                    unlocked = true;
                                    sharedHandler.post(new Runnable(){
                                        @Override
                                        public void run() {
                                            String[] messageParts = new String(message.getPayload()).split(":");
                                            bus.post(new GotMessageEvent(new ChatMessage(messageParts[0],messageParts[1],new Date(Long.valueOf(messageParts[2])))));
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(final Throwable throwable) {
                                    unlocked = true;
                                    sharedHandler.post(new Runnable(){
                                        @Override
                                        public void run() {
                                            bus.post(new ConnectionFailureEvent(throwable));
                                        }
                                    });
                                }
                            });
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                        }
                    });
                }
            }
        },1000,1000);
    }

    @Subscribe
    @Override
    public void onConfigurationChanged(ConfigureDaemonEvent event)
    {
        this.initialize(event.getPayload());
        this.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Chat", " subscribing... ");
                Log.i("Chat", " success subscribing... ");
                MqttTransport.this.start();
            }

            @Override
            public void onFailure(final Throwable throwable) {
                sharedHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bus.post(new ConnectionFailureEvent(throwable));
                    }
                });
            }
        });
    }

    @Subscribe
    @Override
    public void onSendMessage(SendMessageEvent event)
    {
        Log.i("Chat","Sending message {author:"+event.getMessage().getAuthor()+",message:"+event.getMessage().getMessage()+",date:"+event.getMessage().getDate().toString());
        this.safeSendMessage(event.getMessage());
    }


}
