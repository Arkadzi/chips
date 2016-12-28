package me.humennyi.arkadii.chips;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import java.util.Objects;

/**
 * Created by arkadii on 12/21/16.
 */

public class ClickableImageSpanWrapper {
    public static void wrap(SpannableStringBuilder stringBuilder, Drawable drawable, @Nullable final View.OnClickListener listener, int start, int end) {
        stringBuilder.setSpan(new ImageSpan(drawable), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wrap(stringBuilder, listener,start,end);
    }

    public static void wrap(SpannableStringBuilder stringBuilder, @Nullable final View.OnClickListener listener, int start, int end) {
        stringBuilder.setSpan(new ClickableSpanImpl(listener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static class ClickableSpanImpl extends ClickableSpan {
        @Nullable
        private View.OnClickListener onClickListener;

        public ClickableSpanImpl(@Nullable View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public void setOnClickListener(@Nullable View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (onClickListener != null) {
                onClickListener.onClick(widget);
            }
        }
    }
}
