package com.salem.messaging.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.salem.messaging.Module.MessageModul;
import com.salem.messaging.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MessageAdapter  extends ArrayAdapter<MessageModul> {


    public MessageAdapter(Context context, ArrayList<MessageModul> list) {

        super(context, 0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View v = convertView;

        if (v == null) {
            v =LayoutInflater.from(getContext()).inflate(R.layout.message_layout,parent,false);
        }

        MessageModul currMsg = getItem(position);

        TextView messageTextView=v.findViewById(R.id.message_item);
        TextView timeTextView =v.findViewById(R.id.message_time);

        messageTextView.setText(currMsg.getMsg());
        timeTextView.setText(currMsg.getTime());

        return v;
    }
}
