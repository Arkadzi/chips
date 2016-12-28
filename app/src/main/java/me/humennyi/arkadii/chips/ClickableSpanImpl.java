package me.humennyi.arkadii.chips;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.Editable;
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
//                SpannableStringBuilder text = (SpannableStringBuilder) chipsView.getText();
//                Rect bounds = new Rect();
//                Paint tpaint = chipsView.getPaint();
//                tpaint.getTextBounds(text,text.getSpanStart(this),2,bounds);
//                int height = bounds.height();
//                int width = bounds.width();
            }
            onClickListener.onClick(chipsView, this);
        }
    }
}
