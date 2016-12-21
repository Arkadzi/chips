package me.humennyi.arkadii.chips.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.R;

/**
 * Created by arkadii on 12/21/16.
 */

public class ChipsAdapter extends BaseAdapter implements ChipsHandler {
    private final List<Chips> suggestionData = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final Context context;
    private ChipsIdHolder suggestionIdHolder;
    private ChipsIdHolder chipsIdHolder;
    private ChipsIdHolder invalidChipsIdHolder;

    public ChipsAdapter(Context context, ChipsIdHolder suggestionIdHolder, ChipsIdHolder chipsIdHolder, ChipsIdHolder invalidChipsIdHolder, List<Chips> suggestions) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.suggestionIdHolder = suggestionIdHolder;
        this.chipsIdHolder = chipsIdHolder;
        this.invalidChipsIdHolder = invalidChipsIdHolder;
        setSuggestionData(suggestions);
    }

    public void setSuggestionData(@Nullable List<Chips> chips) {
        this.suggestionData.clear();
        if (chips != null) {
            this.suggestionData.addAll(chips);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return suggestionData.size();
    }

    @Override
    public Chips getItem(int position) {
        return suggestionData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ChipsViewHolder viewHolder;
        if (view != null) {
            viewHolder = ((ChipsViewHolder) view.getTag());
        } else {
            viewHolder = ChipsViewHolder.inflate(layoutInflater, suggestionIdHolder, parent);
            view = viewHolder.getView();
        }

        Chips item = getItem(position);
        viewHolder.bind(item);

        return view;
    }

    @Override
    public Drawable getChipsDrawable(Chips chips) {
        ChipsIdHolder idHolder = chips.isValid() ? chipsIdHolder : invalidChipsIdHolder;
        View chipsView = ChipsUtils.inflate(layoutInflater, chips, idHolder);
        Bitmap bitmap = ChipsUtils.convertViewToBitmap(chipsView);

        BitmapDrawable bmpDrawable = new BitmapDrawable(context.getResources(), bitmap);
        bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
        return bmpDrawable;
    }



    @Override
    public View.OnClickListener getChipsClickListener(final Chips chips) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ChipsClick", String.valueOf(chips));
            }
        };
    }


}
