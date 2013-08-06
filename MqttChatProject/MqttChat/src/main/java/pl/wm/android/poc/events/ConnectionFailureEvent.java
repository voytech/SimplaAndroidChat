package pl.wm.android.poc.events;

/**
 * Created by Outbox on 19.07.13.
 */
public class ConnectionFailureEvent {
    private Throwable throwable;
    public ConnectionFailureEvent(Throwable throwable)
    {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
