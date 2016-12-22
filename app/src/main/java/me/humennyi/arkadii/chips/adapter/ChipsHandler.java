package me.humennyi.arkadii.chips.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 12/21/16.
 */

public interface ChipsHandler {
    Drawable getChipsDrawable(Chips chips);
    View.OnClickListener getChipsClickListener(int chipsPosition);
}
