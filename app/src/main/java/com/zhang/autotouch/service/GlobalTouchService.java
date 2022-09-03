package com.zhang.autotouch.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

import androidx.annotation.Nullable;

import com.zhang.autotouch.layout.MyLinearLayout;

public class GlobalTouchService extends Service implements View.OnTouchListener {
    private String TAG = this.getClass().getSimpleName();
    // window manager
    private WindowManager mWindowManager;
    // linear layout will use to detect touch event
    private MyLinearLayout touchLayout;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // create linear layout
        touchLayout = new MyLinearLayout(this);
        // set layout width 30 px and height is equal to full screen
        LayoutParams lp = new LayoutParams(30, LayoutParams.MATCH_PARENT);
        touchLayout.setLayoutParams(lp);
        // set color if you want layout visible on screen
//  touchLayout.setBackgroundColor(Color.CYAN);
        // set on touch listener
        touchLayout.setOnTouchListener(this);

        // fetch window manager object
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // set layout parameter of window manager
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                30, // width of layout 30 px
                WindowManager.LayoutParams.MATCH_PARENT, // height is equal to full screen
                WindowManager.LayoutParams.TYPE_PHONE, // Type Phone, These are non-application windows providing user interaction with the phone (in particular incoming calls).
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // this window won't ever get key input focus
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        Log.i(TAG, "add View");
        // 设置窗体显示类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.width = 30;
            mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.type =WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            mParams.format = PixelFormat.TRANSLUCENT;
        }
        else {
            mParams.width = 30;
            mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.format = PixelFormat.TRANSLUCENT;
        }
        mWindowManager.addView(touchLayout, mParams);

    }

    @Override
    public void onDestroy() {
        if(mWindowManager != null) {
            if(touchLayout != null) mWindowManager.removeView(touchLayout);
        }
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP)
            Log.i(TAG, "Action :" + event.getAction() + "\t X :" + event.getRawX() + "\t Y :"+ event.getRawY());
        Log.i(TAG, "Action :" + event.getAction());
        return true;
    }

}
