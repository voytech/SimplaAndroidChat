package pl.wm.android.poc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Outbox on 18.07.13.
 */
public class ChatMessage implements Parcelable {
    private String message;
    private String author;
    private Date date;

    public ChatMessage(String author,String message,Date date)
    {
        this.message = message;
        this.author = author;
        this.date = date;
    }
    public ChatMessage(Parcel parcel)
    {
        message = parcel.readString();
        author  = parcel.readString();
        date = new Date(parcel.readLong());
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(author);
        parcel.writeLong(date.getTime());
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>(){

        @Override
        public ChatMessage createFromParcel(Parcel parcel) {
            return new ChatMessage(parcel);
        }

        @Override
        public ChatMessage[] newArray(int count) {
            return new ChatMessage[count];
        }

    };
}
