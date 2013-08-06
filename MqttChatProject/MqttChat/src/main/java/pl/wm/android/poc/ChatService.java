package pl.wm.android.poc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Outbox on 20.07.13.
 */
public class ChatService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
