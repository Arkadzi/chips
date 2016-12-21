package me.humennyi.arkadii.chips.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

/**
 * Created by arkadii on 12/21/16.
 */
public class ChipsIdHolder {
    @LayoutRes
    private int layoutId;
    @IdRes
    private int textViewId;
    @IdRes
    private int imageViewId;

    public ChipsIdHolder(int layoutId, int textViewId, int imageViewId) {
        this.layoutId = layoutId;
        this.textViewId = textViewId;
        this.imageViewId = imageViewId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getTextViewId() {
        return textViewId;
    }

    public int getImageViewId() {
        return imageViewId;
    }
}
