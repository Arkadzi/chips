package me.humennyi.arkadii.chips;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by arkadii on 12/20/16.
 */

public class ClickableMovementMethod extends LinkMovementMethod {

    private static ClickableMovementMethod sInstance;

    public static ClickableMovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new ClickableMovementMethod();
        }
        return sInstance;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);

            int off = layout.getOffsetForHorizontal(line, x);

            Log.e("Span", x + " " + y + " " + off);
            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            ClickableImageSpan[] imageSpans = buffer.getSpans(off, off, ClickableImageSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {

                    link[0].onClick(widget);
                } else {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }

                return true;
            } else if (imageSpans.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    imageSpans[0].onClick(widget);
//
//                    Rect bounds = new Rect();
//                    Paint tpaint = widget.getPaint();
//
//                    int spanStart = buffer.getSpanStart(imageSpans[0]);
//                    int spanEnd = buffer.getSpanEnd(imageSpans[0]);
//                    tpaint.getTextBounds(widget.getText().toString(), 0, spanStart, bounds);
//                    int width = bounds.width();

//                    Log.e("MovementMethod", "start = " + width);
                } else {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(imageSpans[0]),
                            buffer.getSpanEnd(imageSpans[0]));
                }

                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }

        return false;
    }
}
