package me.humennyi.arkadii.chips.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsView;
import me.humennyi.arkadii.chips.OnSpanClickListener;

/**
 * Created by arkadii on 12/21/16.
 */

public interface ChipsHandler {
    Drawable getChipsDrawable(Chips chips);
    OnSpanClickListener getChipsClickListener(int chipsPosition, Chips chips);
    @Nullable
    Chips getChipsByText(String text);
}
