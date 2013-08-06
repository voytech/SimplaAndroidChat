package pl.wm.android.poc;

public class ChatConfig{
    private String topic;
    private String host;
    private String clientId;

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    private String me;

    public ChatConfig(String topic,String me,String host,String clientId)
    {
        this.topic = topic;
        this.me = me;
        this.host = host;
        this.clientId = clientId;
    }

    String getTopic() {
        return topic;
    }

    void setTopic(String topic) {
        this.topic = topic;
    }

    String getHost() {
        return host;
    }

    void setHost(String host) {
        this.host = host;
    }

    String getClientId() {
        return clientId;
    }

    void setClientId(String clientId) {
        this.clientId = clientId;
    }

}