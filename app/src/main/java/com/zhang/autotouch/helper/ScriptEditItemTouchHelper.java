package com.zhang.autotouch.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zhang.autotouch.activity.ScriptActivity;
import com.zhang.autotouch.adapter.ScriptEditAdapter;
import com.zhang.autotouch.bean.ScriptItem;

import java.util.Collections;
import java.util.List;

public class ScriptEditItemTouchHelper extends ItemTouchHelper.Callback {
    private List<ScriptItem> list;

    private ScriptEditAdapter scriptEditAdapter;

    private ScriptActivity scriptActivity;

    public ScriptEditItemTouchHelper(List<ScriptItem> list, ScriptEditAdapter scriptEditAdapter, ScriptActivity scriptActivity) {
        this.list = list;
        this.scriptEditAdapter = scriptEditAdapter;
        this.scriptActivity = scriptActivity;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN ;
        final int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition){
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list,i,i+1);
            }
        }else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list,i,i-1);
            }
        }
        scriptEditAdapter.notifyItemMoved(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        list.remove(position);
        scriptEditAdapter.notifyItemRemoved(position);
    }

    public void addLast(ScriptItem item){
        list.add(item);
        scriptEditAdapter.notifyItemInserted(list.size()-1);
    }
}
