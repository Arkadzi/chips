package me.humennyi.arkadii.chips;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * Created by arkadii on 12/21/16.
 */

public class ClickableImageSpanWrapper {
    public static void wrap(SpannableStringBuilder stringBuilder, Drawable drawable, final View.OnClickListener listener, int start, int end) {
        stringBuilder.setSpan(new ImageSpan(drawable), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                listener.onClick(widget);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
