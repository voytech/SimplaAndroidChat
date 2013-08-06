package pl.wm.android.poc;

import android.app.Application;

import dagger.ObjectGraph;
import pl.wm.android.poc.di_modules.ChatDiModule;

/**
 * Created by Outbox on 20.07.13.
 */
public class ChatApplication extends Application{

    private ObjectGraph graph;
    @Override
    public void onCreate()
    {
        super.onCreate();
        graph = ObjectGraph.create(new ChatDiModule(this));
    }
    public void inject(Object injectable)
    {
        graph.inject(injectable);
    }
}
