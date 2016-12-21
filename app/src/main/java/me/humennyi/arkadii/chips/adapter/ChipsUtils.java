package me.humennyi.arkadii.chips.adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.R;

/**
 * Created by arkadii on 12/21/16.
 */

public class ChipsUtils {
    public static Bitmap convertViewToBitmap(View v) {
        if (v.getMeasuredHeight() <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v.measure(specWidth, specWidth);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }

    public static View inflate(LayoutInflater inflater, Chips chips, ChipsIdHolder idHolder) {
        View chipsView = inflater.inflate(idHolder.getLayoutId(), null);
        TextView textView = (TextView) chipsView.findViewById(idHolder.getTextViewId());
        if (textView != null) {
            textView.setText(chips.getText());
        }
        ImageView imageView = (ImageView) chipsView.findViewById(idHolder.getImageViewId());
        if (imageView != null && chips.getDrawableId() > 0) {
            imageView.setImageResource(chips.getDrawableId());
        }
        return chipsView;
    }
}
