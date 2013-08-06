package pl.wm.android.poc;

/**
 * Created by Outbox on 20.07.13.
 */
public class ChatSettings {
    private String transportClass;
    private String myUserName;
    private String serverAddress;

    public String getTransportClass() {
        return transportClass;
    }

    public void setTransportClass(String transportClass) {
        this.transportClass = transportClass;
    }

    public String getMyUserName() {
        return myUserName;
    }

    public void setMyUserName(String myUserName) {
        this.myUserName = myUserName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
