package me.humennyi.arkadii.chips.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 12/21/16.
 */

public class ChipsAdapter extends BaseAdapter implements ChipsHandler {
    private final List<Chips> suggestionData = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private ChipsIdHolder suggestionIdHolder;
    private ChipsIdHolder chipsIdHolder;
    private ChipsIdHolder invalidChipsIdHolder;

    public ChipsAdapter(Context context, ChipsIdHolder suggestionIdHolder, ChipsIdHolder chipsIdHolder, ChipsIdHolder invalidChipsIdHolder, List<Chips> suggestions) {
        layoutInflater = LayoutInflater.from(context);
        this.suggestionIdHolder = suggestionIdHolder;
        this.chipsIdHolder = chipsIdHolder;
        this.invalidChipsIdHolder = invalidChipsIdHolder;
        setSuggestionData(suggestions);
    }

    public void setSuggestionData(@Nullable List<Chips> chips) {
        this.suggestionData.clear();
        if (chips != null) {
            for (int i = 0; i < chips.size(); i++) {
                chips.get(i).setId(i);
            }
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
        return suggestionData.get(position).getId();
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
        return null;
    }

    @Override
    public View.OnClickListener getChipsClickListener(String text) {
        return null;
    }

    @Override
    public Chips getChipsByText(String text) {
        return null;
    }
}
