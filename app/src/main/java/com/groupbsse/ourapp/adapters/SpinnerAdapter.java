package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.groupbsse.ourapp.classes.CustomMessage;

import java.util.List;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class SpinnerAdapter extends ArrayAdapter<CustomMessage> {

    public SpinnerAdapter(Context ctx, List<CustomMessage> messages){
        super(ctx, 0, messages);
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        TextView tv;
        if (convertView == null){
            tv = new TextView(getContext());
        } else {
            tv = (TextView) convertView;
        }
        tv.setTextSize(16);
        final CustomMessage message = getItem(position);
        tv.setText(message.getMessage());
        return tv;
    }
}
