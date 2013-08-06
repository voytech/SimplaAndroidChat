package pl.wm.android.poc;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.Date;

import javax.inject.Inject;
import pl.wm.android.poc.events.ConfigureDaemonEvent;
import pl.wm.android.poc.events.ConnectionFailureEvent;
import pl.wm.android.poc.events.GotMessageEvent;
import pl.wm.android.poc.events.SendMessageEvent;
import pl.wm.android.poc.model.ChatMessage;

public class ChatActivity extends Activity {

    private EditText input;
    private Button button;
    private ListView chatArea;
    private ChatListAdapter adapter;

    @Inject
    Transport mqtt;
    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((ChatApplication)this.getApplication()).inject(this);
        setContentView(R.layout.chat_activity);
        button = (Button)findViewById(R.id.button);
        input = (EditText)findViewById(R.id.editText);
        chatArea = (ListView)findViewById(R.id.chatArea);
        adapter = new ChatListAdapter(this);
        chatArea.setAdapter(adapter);
        assignActions();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        bus.register(this);
        Log.i("Chat", "ChatActivity bus hashCode" + bus.hashCode());
        mqtt.registerSelf();
        bus.post(new ConfigureDaemonEvent(new ChatConfig("free-chat","wojtek","192.168.2.105:8080/websockets_messenger/messenger","wojtek")));
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        bus.unregister(this);
        mqtt.unregisterSelf();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    private void assignActions()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = input.getText().toString();
                String author = "Wojtek";
                Date date = new Date();
                ChatMessage messageObj = new ChatMessage(author,message,date);
                bus.post(new SendMessageEvent(messageObj));
            }
        });
    }

    @Subscribe
    public void onGotMessage(final GotMessageEvent gotMessageEvent)
    {
        adapter.add(gotMessageEvent.getMessage());
    }

    @Subscribe
    public void onConnectionFailureEvent(final ConnectionFailureEvent event)
    {
        Toast.makeText(ChatActivity.this,event.getThrowable().getMessage(), Toast.LENGTH_LONG);
    }
}
