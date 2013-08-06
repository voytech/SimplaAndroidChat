package pl.wm.android.poc;

import android.os.Handler;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pl.wm.android.poc.events.ConfigureDaemonEvent;
import pl.wm.android.poc.events.SendMessageEvent;
import pl.wm.android.poc.model.ChatMessage;
import pl.wm.android.poc.ws.WebSocketClient;

/**
 * Created by Outbox on 22.07.13.
 */
public class WebSocketTransport extends Transport{
    private static final String TAG = "Chat";
    private final Handler handler;
    private ChatConfig config;
    private WebSocketClient client;
    public WebSocketTransport(Bus bus,Handler handler)
    {
        super(bus);
        this.handler = handler;
    }

    private void connect(final Runnable then)
    {

        List<BasicNameValuePair> extraHeaders = Arrays.asList(
                new BasicNameValuePair("Cookie", "session=abcd")
        );
        client = new WebSocketClient(URI.create("ws://"+config.getHost()), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.d(TAG, "Connected!");
                if (then!=null)
                    handler.post(then);
            }

            @Override
            public void onMessage(final String message) {
                Log.d(TAG, String.format("Got string message! %s", message));
                WebSocketTransport.this.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] messageArr = message.split(":");
                        WebSocketTransport.this.received(new ChatMessage(messageArr[0],messageArr[1],new Date(Long.parseLong(messageArr[2]))));
                    }
                });
            }

            @Override
            public void onMessage(byte[] data) {

            }

            @Override
            public void onDisconnect(int code, String reason) {
                Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error!", error);
            }

        }, extraHeaders);
        client.connect();
    }

    @Subscribe
    @Override
    public void onConfigurationChanged(ConfigureDaemonEvent event) {
        this.config = event.getPayload();
        connect(null);
    }

    @Subscribe
    @Override
    public void onSendMessage(final SendMessageEvent send) {
        if (client.isConnected())
            client.send(send.getMessage().getAuthor()+":"+send.getMessage().getMessage()+":"+send.getMessage().getDate().getTime());
        else
            this.connect(new Runnable() {
                @Override
                public void run() {
                    client.send(send.getMessage().getAuthor()+":"+send.getMessage().getMessage()+":"+send.getMessage().getDate().getTime());
                }
            });
    }
}
