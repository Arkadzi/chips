package me.humennyi.arkadii.chips;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * Created by arkadii on 12/20/16.
 */

public abstract class ClickableImageSpan extends ImageSpan {
    public ClickableImageSpan(Drawable d) {
        super(d);
    }

    public abstract void onClick(View view);
}
