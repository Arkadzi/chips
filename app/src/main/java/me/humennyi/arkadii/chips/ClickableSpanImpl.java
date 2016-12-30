package me.humennyi.arkadii.chips;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by arkadii on 12/28/16.
 */
class ClickableSpanImpl extends ClickableSpan {
    @Nullable
    private OnSpanClickListener onClickListener;

    ClickableSpanImpl(@Nullable OnSpanClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    void setOnClickListener(@Nullable OnSpanClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View widget) {
        if (onClickListener != null) {
            Log.e("Widget", String.valueOf(widget.hashCode()));
            ChipsView chipsView = (ChipsView) widget;
            if (chipsView.getText() instanceof SpannableStringBuilder) {
                SpannableStringBuilder text = (SpannableStringBuilder) chipsView.getText();
                Layout layout = chipsView.getLayout();
                int spanStart = text.getSpanStart(this);
                int lineForOffset = layout.getLineForOffset(spanStart);
                int lineHeight = layout.getHeight() / chipsView.getLineCount();
                int xOffset = (int) layout.getPrimaryHorizontal(spanStart);
                int yOffset = lineHeight * (lineForOffset - layout.getLineCount());
                onClickListener.onClick(chipsView, xOffset, yOffset);
            }
        }
    }
}
