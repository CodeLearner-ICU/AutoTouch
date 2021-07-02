package com.zhang.autotouch.utils;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeInfoUtil {

    public static AccessibilityNodeInfo getFocusAccessibilityNodeInfo(AccessibilityService accessibilityService, String focusText) {
        AccessibilityNodeInfo root = accessibilityService.getRootInActiveWindow();
        if (root == null) return null;

        // 1
        AccessibilityNodeInfo info = root.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY);
        // 2
        if (info == null) {
            info = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        }
        // 3
        if (info == null && focusText != null) {
            List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByText(focusText);
            if (nodeInfoList.size() > 0) {
                AccessibilityNodeInfo tmp = nodeInfoList.get(0);
                if (tmp != null) {
                    CharSequence charSequence = tmp.getText();
                    if (charSequence != null && charSequence.equals(focusText)) {
                        info = tmp;
                    }
                }
            }
        }
        // 4
        if (info == null && focusText != null) {
            List<AccessibilityNodeInfo> childList = new ArrayList<>();
            recycle(root, childList);
            if (childList.size() > 0) {
                for (AccessibilityNodeInfo tmp : childList) {
                    if (tmp != null) {
                        CharSequence charSequence = tmp.getText();
                        if (charSequence != null && charSequence.equals(focusText)) {
                            info = tmp;
                        }
                    }
                }
            }
        }
        return info;
    }

    public static AccessibilityNodeInfo getFocusAccessibilityNodeInfoButton(AccessibilityService accessibilityService, String focusText) {
        AccessibilityNodeInfo root = accessibilityService.getRootInActiveWindow();
        if (root == null) return null;

        // 1
        AccessibilityNodeInfo info = null;
        // 3
        if (info == null && focusText != null) {
            List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByText(focusText);
            if (nodeInfoList.size() > 0) {
                AccessibilityNodeInfo tmp = nodeInfoList.get(0);
                if (tmp != null) {
                    CharSequence charSequence = tmp.getText();
                    if (charSequence != null && charSequence.equals(focusText)) {
                        info = tmp;
                    }
                }
            }
        }
        // 4
        if (info == null && focusText != null) {
            List<AccessibilityNodeInfo> childList = new ArrayList<>();
            recycle(root, childList);
            if (childList.size() > 0) {
                for (AccessibilityNodeInfo tmp : childList) {
                    if (tmp != null) {
                        CharSequence charSequence = tmp.getText();
                        if (charSequence != null && charSequence.equals(focusText)) {
                            info = tmp;
                        }
                    }
                }
            }
        }
        return info;
    }

    private static void recycle(AccessibilityNodeInfo root, List<AccessibilityNodeInfo> childList) {
        if (root.getChildCount() == 0) {
            childList.add(root);
        } else {
            for (int i = 0; i < root.getChildCount(); i++) {
                if (root.getChild(i) != null) {
                    recycle(root.getChild(i), childList);
                }
            }
        }
    }

}
