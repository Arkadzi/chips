package me.humennyi.arkadii.chips.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

class ChipsUtils {
    private static Bitmap convertViewToBitmap(View v) {
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

    private static View inflate(LayoutInflater inflater, Chips chips, ChipsIdHolder idHolder) {
        View chipsView = inflater.inflate(idHolder.getLayoutId(), null);
        TextView textView = (TextView) chipsView.findViewById(idHolder.getTextViewId());
        if (textView != null) {
            textView.setText(chips.getText());
        }
        ImageView imageView = (ImageView) chipsView.findViewById(idHolder.getImageViewId());
        if (imageView != null) {
            if(chips.getDrawableId() > 0) {
                imageView.setImageResource(chips.getDrawableId());
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
        return chipsView;
    }

    static Drawable generateDrawable(Context context, LayoutInflater inflater, Chips chips, ChipsIdHolder idHolder) {
        View chipsView = ChipsUtils.inflate(inflater, chips, idHolder);
        Bitmap bitmap = ChipsUtils.convertViewToBitmap(chipsView);
        BitmapDrawable bmpDrawable = new BitmapDrawable(context.getResources(), bitmap);
        bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
        return bmpDrawable;
    }

    public static int measureContentWidth(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, widthMeasureSpec);
        return view.getMeasuredWidth();
    }
}
