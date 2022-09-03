package com.zhang.autotouch.utils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CopyPasteUtil {

    /**
     * 输入文本
     */
    public static void pasteTextOnFocus(AccessibilityService accessibilityService, String focusText, String pasteText) {
        AccessibilityNodeInfo info = AccessibilityNodeInfoUtil.getFocusAccessibilityNodeInfo(accessibilityService, focusText);
        // 复制文字
        if (info != null) {
            pasteText(accessibilityService, info, pasteText);
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private static void pasteText(Context context, AccessibilityNodeInfo info, String text) {
        //粘贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            info.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Bundle arguments = new Bundle();
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, text.length());
            info.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            info.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
            info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }
}
