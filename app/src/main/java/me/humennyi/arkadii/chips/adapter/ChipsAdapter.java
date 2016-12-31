package me.humennyi.arkadii.chips.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsValidator;
import me.humennyi.arkadii.chips.ChipsView;
import me.humennyi.arkadii.chips.OnSpanClickListener;

/**
 * Created by arkadii on 12/21/16.
 */

public abstract class ChipsAdapter<T extends Chips> extends BaseAdapter implements ChipsHandler, Filterable {
    private final List<T> suggestionData = new ArrayList<>();
    private final List<T> currentSuggestions = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final ChipsIdHolder suggestionIdHolder;
    private final ChipsIdHolder chipsIdHolder;
    private final ChipsIdHolder invalidChipsIdHolder;
    @Nullable
    private PopupAdapter popupAdapter;
    @Nullable
    private ListPopupWindow listPopupWindow;
    @Nullable
    private OnChipsClickListener<T> chipsClickListener;
    @Nullable
    private ChipsValidator<T> chipsValidator;

    public ChipsAdapter(Context context,
                        ChipsIdHolder suggestionIdHolder, ChipsIdHolder chipsIdHolder,
                        ChipsIdHolder invalidChipsIdHolder,
                        @Nullable List<T> suggestions, ChipsView chipsView) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.suggestionIdHolder = suggestionIdHolder;
        this.chipsIdHolder = chipsIdHolder;
        this.invalidChipsIdHolder = invalidChipsIdHolder;
        if (hasPopup()) {
            listPopupWindow = new ListPopupWindow(context);
            popupAdapter = new PopupAdapter();
            listPopupWindow.setAdapter(popupAdapter);
            listPopupWindow.setModal(false);
            listPopupWindow.setAnchorView(chipsView);
        }
        setSuggestionData(suggestions);
    }

    public void setSuggestionData(@Nullable List<T> chips) {
        this.suggestionData.clear();
        if (chips != null) {
            this.suggestionData.addAll(chips);
        }
    }

    public boolean hidePopup() {
        if (listPopupWindow != null && listPopupWindow.isShowing()) {
            listPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return currentSuggestions.size();
    }

    @Override
    public T getItem(int position) {
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
    public Drawable getChipsDrawable(Chips chips, boolean isChipsValid) {
        ChipsIdHolder idHolder = isChipsValid ? chipsIdHolder : invalidChipsIdHolder;
        return ChipsUtils.generateDrawable(context, layoutInflater, chips, idHolder);
    }

    @Override
    public OnSpanClickListener getChipsClickListener(final int chipsPosition, final Chips chips) {
        return new OnSpanClickListener() {
            @Override
            public void onClick(ChipsView widget, int xOffset, int yOffset) {
                T typedChips =(T) chips;
                showPopup(typedChips, xOffset, yOffset);
                if (chipsClickListener != null) {
                    chipsClickListener.onChipsClick(chipsPosition, typedChips);
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
    public boolean isChipsValid(Chips chips) {
        return chipsValidator == null || chipsValidator.isValid((T) chips);
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {

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
                    for (T chips : suggestionData) {
                        if (chips.getText().toLowerCase().startsWith(
                                constraint.toString().toLowerCase())) {
                            currentSuggestions.add(chips);
                        }
                    }
                } catch (Exception ignored) {
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

    private void showPopup(T chips, int xOffset, int yOffset) {
        if (popupAdapter != null && listPopupWindow != null) {
            popupAdapter.setChips(chips);
            listPopupWindow.setHorizontalOffset(xOffset);
            listPopupWindow.setVerticalOffset(yOffset);
            listPopupWindow.show();
        }
    }

    public void setChipsClickListener(@Nullable OnChipsClickListener<T> chipsClickListener) {
        this.chipsClickListener = chipsClickListener;
    }

    public void setChipsValidator(@Nullable ChipsValidator<T> chipsValidator) {
        this.chipsValidator = chipsValidator;
    }

    @Nullable
    protected abstract View getPopupView(ViewGroup parent, T item);

    protected abstract boolean hasPopup();

    public interface OnChipsClickListener<T extends Chips> {
        void onChipsClick(int position, T chips);
    }

    private class PopupAdapter extends BaseAdapter {
        private final List<T> singleChips = new ArrayList<>();

        @Override
        public int getCount() {
            return singleChips.size();
        }

        @Override
        public T getItem(int position) {
            return singleChips.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View popupView = getPopupView(parent, this.getItem(position));
            if (listPopupWindow != null) {
                listPopupWindow.setContentWidth(ChipsUtils.measureContentWidth(popupView));
            }
            return popupView;
        }

        public final void setChips(T chips) {
            this.singleChips.clear();
            this.singleChips.add(chips);
        }
    }
}
