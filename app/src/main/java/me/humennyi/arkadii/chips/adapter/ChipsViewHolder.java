package me.humennyi.arkadii.chips.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 12/21/16.
 */

public class ChipsViewHolder {
    private final View view;
    @Nullable
    private final TextView textView;
    @Nullable
    private final ImageView imageView;

    public static ChipsViewHolder inflate(LayoutInflater layoutInflater, ChipsIdHolder idHolder, ViewGroup parent) {
        View view = layoutInflater.inflate(idHolder.getLayoutId(), parent, false);
        return new ChipsViewHolder(view, idHolder);
    }

    private ChipsViewHolder(View view, ChipsIdHolder idHolder) {
        this.view = view;
        textView = (TextView) view.findViewById(idHolder.getTextViewId());
        imageView = (ImageView) view.findViewById(idHolder.getImageViewId());
        view.setTag(this);
    }

    public void bind(Chips item) {
        bindImage(item);
        bindText(item);
    }

    private void bindText(Chips item) {
        String text = item.getText();
        if (textView != null) {
            if (!TextUtils.isEmpty(text)) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(text);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    private void bindImage(Chips item) {
        if (imageView != null) {
            int drawableId = item.getDrawableId();
            if (drawableId > 0) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(drawableId);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    public View getView() {
        return view;
    }
}
