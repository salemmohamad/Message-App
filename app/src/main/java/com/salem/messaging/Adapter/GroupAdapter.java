package com.salem.messaging.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.salem.messaging.Module.GroupModul;
import com.salem.messaging.Module.MessageModul;
import com.salem.messaging.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GroupAdapter extends ArrayAdapter<GroupModul> {
    ArrayList<GroupModul> list;
    public GroupAdapter(Context context, ArrayList<GroupModul> list) {
        super(context,0,list);
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        GroupModul currGroup=list.get(position);

        TextView dayTextView=v.findViewById(R.id.day);
        TextView monthTextView =v.findViewById(R.id.month);
        TextView titleTextView =v.findViewById(R.id.members);

        dayTextView.setText(currGroup.getDay());
        monthTextView.setText(currGroup.getMonth());
        titleTextView.setText(currGroup.getTitle());

        return v;

    }
}
