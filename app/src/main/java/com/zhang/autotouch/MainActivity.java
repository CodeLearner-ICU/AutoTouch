package com.zhang.autotouch;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.zhang.autotouch.activity.ScriptActivity;
import com.zhang.autotouch.fw_permission.FloatWinPermissionCompat;
import com.zhang.autotouch.service.AutoTouchService;
import com.zhang.autotouch.service.FloatingService;
import com.zhang.autotouch.service.GlobalTouchService;
import com.zhang.autotouch.utils.AccessibilityUtil;
import com.zhang.autotouch.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    private TextView tvStart;
    private final String STRING_START = "开始";
    private final String STRING_ACCESS = "无障碍服务";
    private final String STRING_ALERT = "悬浮窗权限";
    private Button btn_script_add_view;
    Intent globalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalService = new Intent(this, GlobalTouchService.class);
        tvStart = findViewById(R.id.tv_start);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tvStart.getText().toString()) {
                    case STRING_START:
                        ToastUtil.show(getString(R.string.app_name) + "已启用");
                        startService(new Intent(MainActivity.this, FloatingService.class));
                        moveTaskToBack(true);
                        break;
                    case STRING_ALERT:
                        requestPermissionAndShow();
                        break;
                    case STRING_ACCESS:
                        requestAcccessibility();
                        break;
                    default:
                        break;
                }
            }
        });
        btn_script_add_view = findViewById(R.id.btn_script_add_view);
        btn_script_add_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScriptActivity.class);
                startActivity(intent);
            }
        });

        ToastUtil.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState();
    }

    private boolean isGuoQi() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date date = formatter.parse("20211001");
            return (date.getTime() - System.currentTimeMillis()) < 0;
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }

    private void checkState() {
        boolean hasAccessibility = AccessibilityUtil.isSettingOpen(AutoTouchService.class, MainActivity.this);
        boolean hasWinPermission = FloatWinPermissionCompat.getInstance().check(this);
        if (hasAccessibility) {
            if (hasWinPermission) {
                tvStart.setText(STRING_START);
            } else {
                tvStart.setText(STRING_ALERT);
            }
        } else {
            tvStart.setText(STRING_ACCESS);
        }
    }

    private void requestAcccessibility() {
        new AlertDialog.Builder(this).setTitle("无障碍服务未开启")
                .setMessage("你的手机没有开启无障碍服务，" + getString(R.string.app_name) + "将无法正常使用")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 显示授权界面
                        try {
                            AccessibilityUtil.jumpToSetting(MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    /**
     * 开启悬浮窗权限
     */
    private void requestPermissionAndShow() {
        new AlertDialog.Builder(this).setTitle("悬浮窗权限未开启")
                .setMessage(getString(R.string.app_name) + "获得悬浮窗权限，才能正常使用应用")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 显示授权界面
                        try {
                            FloatWinPermissionCompat.getInstance().apply(MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    public void buttonClicked(View v){

        if(v.getTag() == null){
            startService(globalService);
            v.setTag("on");
            Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
        }
        else{
            stopService(globalService);
            v.setTag(null);
            Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
        }
    }
}
