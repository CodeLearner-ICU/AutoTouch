package com.zhang.autotouch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhang.autotouch.R;
import com.zhang.autotouch.bean.ScriptItem;

import java.util.List;

public class ScriptListAdapter extends RecyclerView.Adapter<ScriptListAdapter.ScriptListViewHolder> {
    private Context mcontent;

    private List<ScriptItem> items;

    public ScriptListAdapter(Context context, List<ScriptItem> list) {
        this.mcontent = context;
        this.items = list;
    }

    @NonNull
    @Override
    public ScriptListAdapter.ScriptListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScriptListViewHolder(LayoutInflater.from(mcontent).inflate(R.layout.script_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScriptListViewHolder holder, int position) {
        holder.textView.setText(items.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class ScriptListViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public ScriptListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_script_list_item);
        }
    }
}
