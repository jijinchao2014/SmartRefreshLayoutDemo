package com.example.xu.news.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Xu on 2018/4/4
 */

public class ToastUtils {

    private static Toast mToast;

    /**
     * 显示Toast
     */
    public static void showToast(Context c,CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(c, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }


}
