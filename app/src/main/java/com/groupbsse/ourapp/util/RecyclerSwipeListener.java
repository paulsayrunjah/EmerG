package com.groupbsse.ourapp.util;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.groupbsse.ourapp.adapters.AlertAdapter;

/**
 * Created by Sayrunjah on 11/04/2017.
 */
public class RecyclerSwipeListener extends ItemTouchHelper.SimpleCallback {
    RecyclerView.Adapter alertAdapter;
    SwipeListener swipeListener;

    public RecyclerSwipeListener(  RecyclerView.Adapter alertAdapter,SwipeListener swipeListener){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.alertAdapter = alertAdapter;
        this.swipeListener = swipeListener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(swipeListener != null){
            swipeListener.onSiwpe(viewHolder.getAdapterPosition());
        }
    }
    public interface SwipeListener{
        void onSiwpe(int position);
    }
}
