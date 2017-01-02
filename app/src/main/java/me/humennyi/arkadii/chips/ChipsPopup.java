package me.humennyi.arkadii.chips;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

/**
 * Created by arkadii on 1/1/17.
 */

public class ChipsPopup extends ListPopupWindow {

    private PopupWindow popupWindow;

    public ChipsPopup(@NonNull Context context) {
        super(context);
        try {
            Field mPopup = getClass().getSuperclass().getDeclaredField("mPopup");
            mPopup.setAccessible(true);
            popupWindow = (PopupWindow) mPopup.get(this);
        } catch (Exception ignored) {

        }
    }

    public int getPopupHeight() {
        if (popupWindow != null) {
            return popupWindow.getHeight();
        }
        return 0;
    }

    public boolean isAboveAnchor() {
        if (popupWindow != null) {
            return popupWindow.isAboveAnchor();
        } else return false;
    }
}
