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

public class ScriptEditAdapter extends RecyclerView.Adapter<ScriptEditAdapter.ScriptEditViewHolder> {
    private Context mcontent;

    private List<ScriptItem> items;

    public ScriptEditAdapter(Context context,List<ScriptItem> list) {
        this.mcontent = context;
        this.items = list;
    }

    @NonNull
    @Override
    public ScriptEditAdapter.ScriptEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScriptEditViewHolder(LayoutInflater.from(mcontent).inflate(R.layout.script_edit_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScriptEditViewHolder holder, int position) {
        holder.textView.setText(items.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class ScriptEditViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public ScriptEditViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_script_edit_item);
        }
    }
}
