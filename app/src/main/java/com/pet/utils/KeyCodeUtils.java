package com.pet.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 关掉键盘工具类
 */
public class KeyCodeUtils {

    // 关掉输入键盘
    public static void closeKeyCode(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
//	public static void closeKeyCode(Context context, Dialog v) {
//		InputMethodManager imm = (InputMethodManager) context
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		if (imm.isActive()) {
//			imm.hideSoftInputFromWindow(((View) v).getApplicationWindowToken(), 0);
//		}
//	}
}
