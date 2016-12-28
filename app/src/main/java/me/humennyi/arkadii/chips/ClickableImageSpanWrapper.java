package me.humennyi.arkadii.chips;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * Created by arkadii on 12/21/16.
 */

class ClickableImageSpanWrapper {
    static void wrap(SpannableStringBuilder stringBuilder, Drawable drawable, @Nullable final OnSpanClickListener listener, int start, int end) {
        stringBuilder.setSpan(new ImageSpan(drawable), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new ClickableSpanImpl(listener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
