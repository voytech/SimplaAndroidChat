package pl.wm.android.poc;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.wm.android.poc.model.ChatMessage;

/**
 * Created by Outbox on 18.07.13.
 */
public class ChatListAdapter extends ArrayAdapter<ChatMessage> {
    private final LayoutInflater inflater;

    public ChatListAdapter(Context context) {
        super(context,R.layout.chat_list_entry,new ArrayList<ChatMessage>());
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.chat_list_entry,parent,false);
            TextView author = (TextView)convertView.findViewById(R.id.author);
            TextView message = (TextView)convertView.findViewById(R.id.message);
            TextView date = (TextView)convertView.findViewById(R.id.date);
            convertView.setTag(new Wrapper(author,date,message));
        }
        ChatMessage cmessage = this.getItem(position);
        Wrapper wrapper = (Wrapper)convertView.getTag();
        wrapper.getAuthor().setText(cmessage.getAuthor());
        wrapper.getDate().setText(cmessage.getDate().toString());
        wrapper.getMessage().setText(cmessage.getMessage());
        return convertView;
    }

    class Wrapper
    {
        private TextView author;
        private TextView date;
        private TextView message;

        public Wrapper(TextView author,TextView date,TextView message)
        {
            this.author  = author;
            this.date    = date;
            this.message =message;
        }

        TextView getAuthor() {
            return author;
        }

        void setAuthor(TextView author) {
            this.author = author;
        }

        TextView getDate() {
            return date;
        }

        void setDate(TextView date) {
            this.date = date;
        }

        TextView getMessage() {
            return message;
        }

        void setMessage(TextView message) {
            this.message = message;
        }
    }
}
