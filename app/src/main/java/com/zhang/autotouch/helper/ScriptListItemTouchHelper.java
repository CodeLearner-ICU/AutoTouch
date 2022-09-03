package com.zhang.autotouch.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zhang.autotouch.adapter.ScriptListAdapter;
import com.zhang.autotouch.bean.ScriptItem;

import java.util.List;

public class ScriptListItemTouchHelper extends ItemTouchHelper.Callback {
    private List<ScriptItem> list;

    private ScriptListAdapter scriptListAdapter;

    private ScriptEditItemTouchHelper editItemTouchHelper;

    public ScriptListItemTouchHelper(List<ScriptItem> list, ScriptListAdapter scriptListAdapter, ScriptEditItemTouchHelper editItemTouchHelper) {
        this.list = list;
        this.scriptListAdapter = scriptListAdapter;
        this.editItemTouchHelper = editItemTouchHelper;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = 0;
        final int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        editItemTouchHelper.addLast(list.get(position));
        scriptListAdapter.notifyItemChanged(position);
    }
}
