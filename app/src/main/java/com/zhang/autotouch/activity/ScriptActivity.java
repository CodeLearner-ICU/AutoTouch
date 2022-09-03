package com.zhang.autotouch.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhang.autotouch.R;
import com.zhang.autotouch.adapter.ScriptEditAdapter;
import com.zhang.autotouch.adapter.ScriptListAdapter;
import com.zhang.autotouch.bean.ScriptItem;
import com.zhang.autotouch.helper.ScriptEditItemTouchHelper;
import com.zhang.autotouch.helper.ScriptListItemTouchHelper;

import java.util.ArrayList;

public class ScriptActivity extends AppCompatActivity {
    private RecyclerView rvEdit;

    private RecyclerView rvList;

    public RecyclerView getRvEdit() {
        return rvEdit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);
        rvEdit = findViewById(R.id.rv_edit);
        rvEdit.setLayoutManager(new LinearLayoutManager(ScriptActivity.this));
        ArrayList<ScriptItem> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ScriptItem scriptItem = new ScriptItem();
            scriptItem.setName("name"+ i);
            list.add(scriptItem);
        }
        ScriptEditAdapter scriptEditAdapter = new ScriptEditAdapter(ScriptActivity.this, list);
        rvEdit.setAdapter(scriptEditAdapter);
        ScriptEditItemTouchHelper editItemTouchHelper = new ScriptEditItemTouchHelper(list, scriptEditAdapter, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(editItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvEdit);

        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(ScriptActivity.this));
        ArrayList<ScriptItem> list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ScriptItem scriptItem = new ScriptItem();
            scriptItem.setName("item-"+ i);
            list2.add(scriptItem);
        }
        ScriptListAdapter scriptListAdapter = new ScriptListAdapter(ScriptActivity.this, list2);
        rvList.setAdapter(scriptListAdapter);
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(new ScriptListItemTouchHelper(list2,scriptListAdapter,editItemTouchHelper));
        itemTouchHelper2.attachToRecyclerView(rvList);
    }
}