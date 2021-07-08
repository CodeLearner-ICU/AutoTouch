package com.zhang.autotouch.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.zhang.autotouch.R;
import com.zhang.autotouch.TouchEventManager;
import com.zhang.autotouch.bean.TouchEvent;
import com.zhang.autotouch.bean.TouchPoint;
import com.zhang.autotouch.utils.AccessibilityNodeInfoUtil;
import com.zhang.autotouch.utils.CopyPasteUtil;
import com.zhang.autotouch.utils.DensityUtil;
import com.zhang.autotouch.utils.KeyboardDownUtil;
import com.zhang.autotouch.utils.LoopArrayList;
import com.zhang.autotouch.utils.SpUtils;
import com.zhang.autotouch.utils.WindowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 无障碍服务-自动点击
 *
 * @date 2019/9/6 16:23
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class AutoTouchService extends AccessibilityService {

    private final String TAG = "AutoTouchService+++";
    private final List<TouchPoint> autoTouchPointList = new LoopArrayList<>();
    private int index = 0;
    private final DecimalFormat floatDf = new DecimalFormat("#0.0");
    //自动点击事件
    private TouchPoint autoTouchPoint;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper());
    private WindowManager windowManager;
    private TextView tvTouchPoint;
    //倒计时
    private float countDownTime;
    //修改点击文本的倒计时
    private Runnable touchViewRunnable;
    private final Runnable autoTouchRunnable = new Runnable() {
        @Override
        public void run() {

//            if (!TextUtils.isEmpty(autoTouchPoint.getText())) {
//                AccessibilityNodeInfo nodeInfo
//                        = AccessibilityNodeInfoUtil.getFocusAccessibilityNodeInfoButton(AutoTouchService.this, "发送");
//                if (nodeInfo != null) {
//                    CopyPasteUtil.pasteTextOnFocus(
//                            AutoTouchService.this
//                            , null
//                            , autoTouchPoint.getText() == null ? "赞👍" : autoTouchPoint.getText()
//                    );
//                    Rect rect = new Rect();
//                    nodeInfo.getBoundsInScreen(rect);
//                    Log.d(TAG, "forceClick: " + rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom);
//                    int x = (rect.left + rect.right) / 2;
//                    int y = (rect.top + rect.bottom) / 2;
//                    keyDownMethod(new TouchPoint("发送", x, y, autoTouchPoint.getDelay()));
//                } else {
//                    KeyboardDownUtil.keyDown(KeyEvent.KEYCODE_ENTER);
//                }
//            }
            keyDownMethod(autoTouchPoint);
            onAutoClick();
        }
    };

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        handler = new Handler();
        EventBus.getDefault().register(this);
        windowManager = WindowUtils.getWindowManager(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReciverTouchEvent(TouchEvent event) {
        Log.d(TAG, "onReciverTouchEvent: " + event.toString());
        TouchEventManager.getInstance().setTouchAction(event.getAction());
        handler.removeCallbacks(autoTouchRunnable);
        switch (event.getAction()) {
            case TouchEvent.ACTION_START:
                autoTouchPointList.clear();
                if (event.getTouchPoint() == null) {
                    autoTouchPointList.addAll(SpUtils.getTouchPoints(AutoTouchService.this));
                    if (autoTouchPointList.size() == 0) {
                        return;
                    }
                } else {
                    autoTouchPointList.add(event.getTouchPoint());
                }
                index = 0;
                onAutoClick();
                break;
            case TouchEvent.ACTION_CONTINUE:
                if (autoTouchPoint != null) {
                    onAutoClick();
                }
                break;
            case TouchEvent.ACTION_PAUSE:
                handler.removeCallbacks(autoTouchRunnable);
                handler.removeCallbacks(touchViewRunnable);
                break;
            case TouchEvent.ACTION_STOP:
                handler.removeCallbacks(autoTouchRunnable);
                handler.removeCallbacks(touchViewRunnable);
                removeTouchView();
                autoTouchPoint = null;
                break;
        }
    }

    /**
     * 执行自动点击
     */
    private void onAutoClick() {
        autoTouchPoint = autoTouchPointList.get(index++);
        if (autoTouchPoint != null) {
            handler.postDelayed(autoTouchRunnable, getDelayTime());
            showTouchView();
            Log.e("自动点击", "自动点击");
        }
    }

    private void keyDownMethod(TouchPoint autoTouchPoint) {
        Log.d(TAG, "onAutoClick: " + "x=" + autoTouchPoint.getX() + " y=" + autoTouchPoint.getY());
        Path path = new Path();
        float rand = new Random().nextFloat();
        path.moveTo(autoTouchPoint.getX() + rand, autoTouchPoint.getY() + rand);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 0, 100))
                .build();
        dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d("AutoTouchService", "滑动结束" + gestureDescription.getStrokeCount());
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d("AutoTouchService", "滑动取消");
            }
        }, null);
    }

    private long getDelayTime() {
//        int random = (int) (Math.random() * (30 - 1) + 1);
//        return autoTouchEvent.getDelay() * 1000L + random;
        float f = new Random().nextFloat();
        long current = (long) (500 + f * 500L);
        return autoTouchPoint.getDelay() * current;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        removeTouchView();
    }

    /**
     * 显示倒计时
     */
    private void showTouchView() {
        if (autoTouchPoint != null) {
            //创建触摸点View
            if (tvTouchPoint == null) {
                tvTouchPoint = (TextView) LayoutInflater.from(this).inflate(R.layout.window_touch_point, null);
            }
            //显示触摸点View
            if (windowManager != null && !tvTouchPoint.isAttachedToWindow()) {
                int width = DensityUtil.dip2px(this, 40);
                int height = DensityUtil.dip2px(this, 40);
                WindowManager.LayoutParams params = WindowUtils.newWmParams(width, height);
                params.gravity = Gravity.START | Gravity.TOP;
                params.x = autoTouchPoint.getX() - width / 2;
                params.y = autoTouchPoint.getY() - width;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                windowManager.addView(tvTouchPoint, params);
            }
            //开启倒计时
            countDownTime = autoTouchPoint.getDelay();
            if (touchViewRunnable == null) {
                touchViewRunnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(touchViewRunnable);
                        Log.d("触摸倒计时", countDownTime + "");
                        if (countDownTime > 0) {
                            float offset = 0.1f;
                            tvTouchPoint.setText(floatDf.format(countDownTime));
                            countDownTime -= offset;
                            handler.postDelayed(touchViewRunnable, (long) (500L * offset));
                        } else {
                            removeTouchView();
                        }
                    }
                };
            }
            handler.post(touchViewRunnable);
        }
    }

    private void removeTouchView() {
        if (windowManager != null && tvTouchPoint != null && tvTouchPoint.isAttachedToWindow()) {
            windowManager.removeView(tvTouchPoint);
        }
    }
}
