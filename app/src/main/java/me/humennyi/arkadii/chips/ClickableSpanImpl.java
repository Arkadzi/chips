package me.humennyi.arkadii.chips;

import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;

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
            ChipsView chipsView = (ChipsView) widget;
            if (chipsView.getText() instanceof SpannableStringBuilder) {
                SpannableStringBuilder text = (SpannableStringBuilder) chipsView.getText();
                Layout layout = chipsView.getLayout();
                int spanStart = text.getSpanStart(this);
                int lineForOffset = layout.getLineForOffset(spanStart);
                int lineHeight = layout.getHeight() / chipsView.getLineCount();
                int xOffset = (int) layout.getPrimaryHorizontal(spanStart);
                int yOffsetIfBelow = lineHeight * (lineForOffset - layout.getLineCount() + 1);
                int yOffsetIfAbove = -lineHeight * (lineForOffset + 1) / 2;
                onClickListener.onClick(chipsView, xOffset, yOffsetIfBelow, yOffsetIfAbove);
            }
        }
    }
}
