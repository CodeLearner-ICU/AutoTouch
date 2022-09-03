package com.zhang.autotouch.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import com.zhang.autotouch.R;
import com.zhang.autotouch.bean.TouchPoint;
import com.zhang.autotouch.utils.SpUtils;
import com.zhang.autotouch.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddPointDialog extends BaseServiceDialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private EditText etName;
    private EditText etTime;
    private EditText etText;
    private Group groupInput;
    private TextView tvHint;
    private int x;
    private int y;
    private RadioButton radiobutton_2;

    public AddPointDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_add_point;
    }

    @Override
    protected int getWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int getHeight() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected void onInited() {
        etName = findViewById(R.id.et_name);
        SimpleDateFormat f = new SimpleDateFormat( "yyyy年MM月dd日HH点mm分ss秒", Locale.getDefault());
        etName.setText(f.format(new Date()));
        etTime = findViewById(R.id.et_time);
        etText = findViewById(R.id.et_text);
        etText.setVisibility(View.INVISIBLE);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        RadioButton radiobutton_1 = findViewById(R.id.radiobutton_1);
        radiobutton_2 = findViewById(R.id.radiobutton_2);
        radiobutton_2.setChecked(false);
        radiobutton_1.setChecked(true);
        radioGroup.setOnCheckedChangeListener(this);
        etTime.setText("1");
        groupInput = findViewById(R.id.gl_input);
        tvHint = findViewById(R.id.tv_hint);
        findViewById(R.id.bt_commit).setOnClickListener(this);
        findViewById(R.id.bt_cancel).setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x = (int) event.getRawX();
            y = (int) event.getRawY();
            tvHint.setVisibility(View.GONE);
            groupInput.setVisibility(View.VISIBLE);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_commit:
                String name = null;
                int second = 0;
                try {
                    name = etName.getText().toString().trim();
                    second = Integer.parseInt(etTime.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(name) || second <= 0) {
                    ToastUtil.show("名字或秒数错误");
                    return;
                }
                String text = null;
                if (radiobutton_2.isChecked()) {
                    text = etText.getText().toString().trim();
                    if (TextUtils.isEmpty(text)) {
                        ToastUtil.show("评论模式必须输入评论文字");
                        return;
                    }
                }

                TouchPoint touchPoint = new TouchPoint(name, x, y, second, text);
                SpUtils.addTouchPoint(getContext(), touchPoint);
                dismiss();
                break;
            case R.id.bt_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radiobutton_1:
                etText.setText("");
                etText.setVisibility(View.INVISIBLE);
                break;
            case R.id.radiobutton_2:
                etText.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
