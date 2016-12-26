package me.humennyi.arkadii.chips.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 12/21/16.
 */

public class ChipsAdapter extends BaseAdapter implements ChipsHandler, Filterable {
    private final List<Chips> suggestionData = new ArrayList<>();
    private final List<Chips> currentSuggestions = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final Context context;
    private ChipsIdHolder suggestionIdHolder;
    private ChipsIdHolder chipsIdHolder;
    private ChipsIdHolder invalidChipsIdHolder;
    @Nullable
    private OnChipsClickListener chipsClickListener;

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
    }

    @Override
    public int getCount() {
        return currentSuggestions.size();
    }

    @Override
    public Chips getItem(int position) {
        return currentSuggestions.get(position);
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

        viewHolder.bind(getItem(position));

        return view;
    }

    @Override
    public Drawable getChipsDrawable(Chips chips) {
        ChipsIdHolder idHolder = chips.isValid() ? chipsIdHolder : invalidChipsIdHolder;
        return ChipsUtils.generateDrawable(context, layoutInflater, chips, idHolder);
    }


    @Override
    public View.OnClickListener getChipsClickListener(final int chipsPosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipsClickListener != null) {
                    chipsClickListener.onChipsClick(chipsPosition);
                }
            }
        };
    }

    @Nullable
    @Override
    public Chips getChipsByText(String text) {
        for (Chips chips : suggestionData) {
            if (chips.getText().equalsIgnoreCase(text)) {
                return chips;
            }
        }
        return null;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Chips) resultValue).getText();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                currentSuggestions.clear();
                try {
                    for (Chips chips : suggestionData) {
                        if (chips.getText().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            currentSuggestions.add(chips);
                        }
                    }
                } catch (Exception e) {
                }
                filterResults.values = currentSuggestions;
                filterResults.count = currentSuggestions.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    public void setChipsClickListener(@Nullable OnChipsClickListener chipsClickListener) {
        this.chipsClickListener = chipsClickListener;
    }

    public interface OnChipsClickListener {
        void onChipsClick(int position);
    }
}
